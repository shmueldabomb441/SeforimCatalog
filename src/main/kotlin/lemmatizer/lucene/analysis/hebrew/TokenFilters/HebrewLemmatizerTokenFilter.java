package lemmatizer.lucene.analysis.hebrew.TokenFilters;

import lemmatizer.hebmorph.*;
import lemmatizer.hebmorph.datastructures.DictHebMorph;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import lemmatizer.lucene.analysis.hebrew.HebrewPosAttribute;
import lemmatizer.lucene.analysis.hebrew.HebrewTokenTypeAttribute;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

import java.io.IOException;
import java.util.*;

/**
 * Created by Egozy on 12/04/2015.
 */
public final class HebrewLemmatizerTokenFilter extends TokenFilter {
    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
    private final PositionIncrementAttribute posIncrAtt = addAttribute(PositionIncrementAttribute.class);
    private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
    private final HebrewTokenTypeAttribute hebrewTypeAtt = addAttribute(HebrewTokenTypeAttribute.class);
    private final HebrewPosAttribute posAtt = addAttribute(HebrewPosAttribute.class);

    private Lemmatizer lemmatizer;

    private List<Token> previousLemmas = new ArrayList<>();
    private int previousStartOffset, previousEndOffset;
    private boolean previousTolerated = false, lemmatizeExactHebrewWords, lemmatizeExactNonHebrewWords;
    private HebrewTokenTypeAttribute.HebrewType previousType;

    private final Set<String> duplicateLemmas = new HashSet<>(20); // this stores lemmas we've seen for a word, to remove duplicated lemmas
    private final List<HebrewToken> tokensList = new ArrayList<>(20);

    public HebrewLemmatizerTokenFilter(TokenStream input, DictHebMorph dict) {
        this(input, dict, true, true);
    }

    public HebrewLemmatizerTokenFilter(TokenStream input, DictHebMorph dict, boolean lemmatizeExactHebrewWords, boolean lemmatizeExactNonHebrewWords) {
        super(input);
        this.lemmatizer = new Lemmatizer(dict);
        this.lemmatizeExactHebrewWords = lemmatizeExactHebrewWords;
        this.lemmatizeExactNonHebrewWords = lemmatizeExactNonHebrewWords;
    }

    @Override
    public boolean incrementToken() throws IOException {
        // first check if we have leftover lemmas

        if (!previousLemmas.isEmpty()) {
            clearAttributes();
            String tokenVal;
            // Index all unique lemmas at the same position
            if (previousType == HebrewTokenTypeAttribute.HebrewType.Hebrew ||
                    previousType == HebrewTokenTypeAttribute.HebrewType.Acronym ||
                    previousType == HebrewTokenTypeAttribute.HebrewType.Construct) {
                HebrewToken hebToken = (HebrewToken) previousLemmas.remove(0);
                tokenVal = hebToken.getLemma() == null ? hebToken.getText().substring(hebToken.getPrefixLength()) : hebToken.getLemma();
                posAtt.setHebrewToken(hebToken);
            } else {
                tokenVal = previousLemmas.remove(0).getText();
                posAtt.setHebrewToken(null);
            }
            termAtt.setEmpty().append(tokenVal);
            hebrewTypeAtt.setType(HebrewTokenTypeAttribute.HebrewType.Lemma);
            posIncrAtt.setPositionIncrement(0);
            offsetAtt.setOffset(previousStartOffset, previousEndOffset);
            return true;
        }

        // if input is over, so are we
        if (!input.incrementToken()) {
            return false;
        }
        // we do not lemmatize numbers or exact words (if specified)
        if (hebrewTypeAtt.isNumeric() || (hebrewTypeAtt.isExact() &&
                ((!lemmatizeExactHebrewWords && hebrewTypeAtt.isHebrew()) ||
                        (!lemmatizeExactNonHebrewWords && hebrewTypeAtt.getType() == HebrewTokenTypeAttribute.HebrewType.NonHebrew))))
        {
            return true;
        }

        previousLemmas.clear();
        duplicateLemmas.clear();
        previousStartOffset = offsetAtt.startOffset();
        previousEndOffset = offsetAtt.endOffset();
        previousType = hebrewTypeAtt.getType();

        // Handle hebrew words
        if (hebrewTypeAtt.isHebrew()) {
            previousTolerated = false;

            String word = termAtt.toString();

            // try to lemmatize
            tokensList.clear();
            lemmatizer.lemmatize(word, tokensList);

            // word wasn't found in the dictionary - try tolerating it
            if (tokensList.isEmpty()) {
                lemmatizer.lemmatizeTolerant(word, tokensList);
                previousTolerated = true;
            }
            tokensList.sort(Comparator.reverseOrder());

            // add words to the previousLemmas : remove duplicates and tokens which aren't ranked high enough if they came from tolerating.
            // TODO: consider the ranking as an additional filter
            for (HebrewToken hebToken : tokensList) {
                if (isValidToken(hebToken) || !previousTolerated) {
                    if (duplicateLemmas.add(hebToken.getLemma())) {
                        previousLemmas.add(hebToken);
                    }
                }
            }
            // TODO:
            // this is just a workaround - current analyzer adds all low-rated tokens if no token is high enough
            if (!tokensList.isEmpty() && previousLemmas.isEmpty()) {
                for (HebrewToken hebToken : tokensList) {
                    if (duplicateLemmas.add(hebToken.getLemma())) {
                        previousLemmas.add(hebToken);
                    }
                }
            }
            // word wasn't found even after toleration - add itself as the only lemma
            if (previousLemmas.isEmpty()) {
                previousLemmas.add(new HebrewToken(termAtt.toString(), (byte) 0, DescFlag.D_EMPTY, word, PrefixType.PS_EMPTY, 1.0f));
            }
            // TODO: merge lemmas that have the same properties or text
            // we built a lemmaslist, this currently returns the original words. Consecutive calls to incrementToken() will return it's lemmas.
        } else {
            //Handle non-hebrew words
            previousLemmas.add(new Token(termAtt.toString()));
        }

        return true;
    }

    @Override
    public void reset() throws IOException {
        super.reset();
    }

    public boolean isValidToken(final HebrewToken t) {
        // Pose a minimum score limit for words
        if (t.getScore() < 0.7f) {
            return false;
        }
        // Pose a higher threshold to verbs (easier to get irrelevant verbs from toleration)
        if ((t.getMask() == DescFlag.D_VERB) && (t.getScore() < 0.85f)) {
            return false;
        }
        return true;
    }
}
