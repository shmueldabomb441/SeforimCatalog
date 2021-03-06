/***************************************************************************
 *   Copyright (C) 2010-2015 by                                            *
 *      Itamar Syn-Hershko <itamar at code972 dot com>                     *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU Affero General Public License           *
 *   version 3, as published by the Free Software Foundation.              *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU Affero General Public License for more details.                   *
 *                                                                         *
 *   You should have received a copy of the GNU Affero General Public      *
 *   License along with this program; if not, see                          *
 *   <http://www.gnu.org/licenses/>.                                       *
 **************************************************************************/
package lemmatizer.lucene.analysis.hebrew;

import lemmatizer.hebmorph.*;
import lemmatizer.hebmorph.datastructures.DictHebMorph;
import lemmatizer.hebmorph.datastructures.DictRadix;
import lemmatizer.hebmorph.lemmafilters.LemmaFilterBase;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.CharacterUtils;
import lemmatizer.lucene.analysis.hebrew.HebrewPosAttribute;
import lemmatizer.lucene.analysis.hebrew.HebrewTokenizer;
import org.apache.lucene.analysis.tokenattributes.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StreamLemmasFilter extends org.apache.lucene.analysis.Tokenizer {
    private final StreamLemmatizer _streamLemmatizer;
    private final CharArraySet commonWords;

    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
    private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
    private final PositionIncrementAttribute posIncrAtt = addAttribute(PositionIncrementAttribute.class);
    private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);
    private final KeywordAttribute keywordAtt = addAttribute(KeywordAttribute.class);
    private final lemmatizer.lucene.analysis.hebrew.HebrewPosAttribute posAtt = addAttribute(HebrewPosAttribute.class);

    private final LemmaFilterBase lemmaFilter;
    private final List<Token> stack = new ArrayList<Token>();
    private final List<Token> filterCache = new ArrayList<Token>();
    private int index = 0;
    private final Set<String> previousLemmas = new HashSet<String>();
    private boolean keepOriginalWord;

    public StreamLemmasFilter(final DictHebMorph dict) {
        this(dict, null, null, null);
    }

    public StreamLemmasFilter(final DictHebMorph dict, final CharArraySet commonWords, final LemmaFilterBase lemmaFilter) {
        this(dict, null, commonWords, lemmaFilter);
    }

    public StreamLemmasFilter(DictHebMorph dict,
                              DictRadix<Byte> specialTokenizationCases, CharArraySet commonWords, LemmaFilterBase lemmaFilter) {
        super();
        _streamLemmatizer = new StreamLemmatizer(input, dict, specialTokenizationCases);
        this.commonWords = commonWords != null ? commonWords : CharArraySet.EMPTY_SET;
        this.lemmaFilter = lemmaFilter;
    }

    public void setSuffixForExactMatch(Character c) {
        _streamLemmatizer.setSuffixForExactMatch(c);
    }

    private final Reference<String> tempRefObject = new Reference<>("");

    private int currentStartOffset, currentEndOffset;

    @Override
    public final boolean incrementToken() throws IOException {
        clearAttributes();

        // Index all unique lemmas at the same position
        while (index < stack.size()) {
            final HebrewToken res = (HebrewToken) ((stack.get(index) instanceof HebrewToken) ? stack.get(index) : null);
            index++;

            if ((res == null) || !previousLemmas.add(res.getLemma())) // Skip multiple lemmas (we will merge morph properties later)
                continue;

            createHebrewToken(res);
            offsetAtt.setOffset(currentStartOffset, currentEndOffset);
            typeAtt.setType(lemmatizer.lucene.analysis.hebrew.HebrewTokenizer.tokenTypeSignature(lemmatizer.lucene.analysis.hebrew.HebrewTokenizer.TOKEN_TYPES.Hebrew));
            posIncrAtt.setPositionIncrement(0);

            return true;
        }

        // Reset state
        index = 0;
        stack.clear();
        previousLemmas.clear();

        // Lemmatize next word in stream. The HebMorph lemmatizer will always return a token, unless
        // an unrecognized Hebrew word is hit, then an empty tokens array will be returned.
        final int tokenType = _streamLemmatizer.getLemmatizeNextToken(tempRefObject, stack);
        if (tokenType == 0) { // EOS
            return false;
        }

        // Store the location of the word in the original stream
        currentStartOffset = correctOffset(_streamLemmatizer.getStartOffset());
        currentEndOffset = correctOffset(_streamLemmatizer.getEndOffset());
        offsetAtt.setOffset(currentStartOffset, currentEndOffset);

        final String word = tempRefObject.ref;
        if (commonWords.contains(word)) { // common words should be treated later using dedicated filters
            termAtt.copyBuffer(word.toCharArray(), 0, word.length());
            typeAtt.setType(lemmatizer.lucene.analysis.hebrew.HebrewTokenizer.tokenTypeSignature(lemmatizer.lucene.analysis.hebrew.HebrewTokenizer.TOKEN_TYPES.Hebrew));
            stack.clear();

            if (!keepOriginalWord) {
                if ((tokenType & Tokenizer.TokenType.Exact) > 0) {
                    keywordAtt.setKeyword(true);
                }
                return true;
            }

            keywordAtt.setKeyword(true);
            if ((tokenType & Tokenizer.TokenType.Exact) == 0) {
                stack.add(new HebrewToken(word, (byte) 0, DescFlag.D_EMPTY, word, PrefixType.PS_EMPTY, 1.0f));
            }

            return true;
        }

        // Mark request for exact matches in queries, if configured in the tokenizer
        if ((tokenType & Tokenizer.TokenType.Exact) > 0) {
            keywordAtt.setKeyword(true);
        }

        // A non-Hebrew word
        if (stack.size() == 1 && !(stack.get(0) instanceof HebrewToken)) {
            termAtt.copyBuffer(word.toCharArray(), 0, word.length());

            final Token tkn = stack.get(0);
            if (tkn.isNumeric()) {
                typeAtt.setType(lemmatizer.lucene.analysis.hebrew.HebrewTokenizer.tokenTypeSignature(lemmatizer.lucene.analysis.hebrew.HebrewTokenizer.TOKEN_TYPES.Numeric));
            } else {
                typeAtt.setType(lemmatizer.lucene.analysis.hebrew.HebrewTokenizer.tokenTypeSignature(lemmatizer.lucene.analysis.hebrew.HebrewTokenizer.TOKEN_TYPES.NonHebrew));
            }

            applyLowercaseFilter();

            stack.clear();
            return true;
        }

        // If we arrived here, we hit a Hebrew word
        typeAtt.setType(lemmatizer.lucene.analysis.hebrew.HebrewTokenizer.tokenTypeSignature(lemmatizer.lucene.analysis.hebrew.HebrewTokenizer.TOKEN_TYPES.Hebrew));

        // Do some filtering if requested...
        if (lemmaFilter != null && lemmaFilter.filterCollection(word, stack, filterCache) != null) {
            stack.clear();
            stack.addAll(filterCache);
        }

        // OOV case - store the word as-is, and also output a suffixed version of it
        if (stack.isEmpty()) {
            termAtt.copyBuffer(word.toCharArray(), 0, word.length());

            if (keepOriginalWord) {
                keywordAtt.setKeyword(true);
            }

            if ((tokenType & Tokenizer.TokenType.Mixed) > 0) {
                typeAtt.setType(lemmatizer.lucene.analysis.hebrew.HebrewTokenizer.tokenTypeSignature(HebrewTokenizer.TOKEN_TYPES.Mixed));
                applyLowercaseFilter();
                return true;
            }
            if ((tokenType & Tokenizer.TokenType.Exact) > 0) {
                applyLowercaseFilter();
                return true;
            }

            if (keepOriginalWord)
                stack.add(new HebrewToken(word, (byte) 0, DescFlag.D_EMPTY, word, PrefixType.PS_EMPTY, 1.0f));

            return true;
        }

        // Mark and store the original term to increase precision, while all lemmas
        // will be popped out of the stack and get stored at the next call to IncrementToken.
        if (keepOriginalWord) {
            termAtt.copyBuffer(word.toCharArray(), 0, word.length());
            keywordAtt.setKeyword(true);
            return true;
        }

        // If !keepOriginalWord
        final HebrewToken hebToken = (HebrewToken) stack.get(0);
        if (stack.size() == 1) { // only one lemma was found
            stack.clear();
        } else { // // more than one lemma exist.
            index = 1;
            previousLemmas.add(hebToken.getLemma());
        }
        createHebrewToken(hebToken);

        return true;
    }

    private void applyLowercaseFilter() {
        CharacterUtils.toLowerCase(termAtt.buffer(), 0, termAtt.length());
    }

    protected void createHebrewToken(HebrewToken hebToken) {
        String tokenVal = hebToken.getLemma() == null ? hebToken.getText().substring(hebToken.getPrefixLength()) : hebToken.getLemma();
        termAtt.copyBuffer(tokenVal.toCharArray(), 0, tokenVal.length());
        posAtt.setHebrewToken(hebToken);
    }

    @Override
    public final void end() throws IOException {
        super.end();
        // set final offset
        int finalOffset = correctOffset(_streamLemmatizer.getEndOffset());
        currentStartOffset = currentEndOffset = finalOffset;
        offsetAtt.setOffset(finalOffset, finalOffset);
    }

    @Override
    public void close() throws IOException {
        super.close();
        stack.clear();
        filterCache.clear();
        previousLemmas.clear();
        index = 0;
        _streamLemmatizer.reset(input);
    }

    @Override
    public void reset() throws IOException {
        super.reset();
        stack.clear();
        filterCache.clear();
        previousLemmas.clear();
        index = 0;
        currentStartOffset = currentEndOffset = 0;
        _streamLemmatizer.reset(input);
    }

    public void setKeepOriginalWord(boolean keepOriginalWord) {
        this.keepOriginalWord = keepOriginalWord;
    }
}
