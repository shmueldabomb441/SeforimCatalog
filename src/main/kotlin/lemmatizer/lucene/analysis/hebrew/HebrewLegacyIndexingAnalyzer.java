/**
 * ************************************************************************
 * Copyright (C) 2010-2015 by                                            *
 * Itamar Syn-Hershko <itamar at code972 dot com>                     *
 * *
 * This program is free software; you can redistribute it and/or modify  *
 * it under the terms of the GNU Affero General Public License           *
 * version 3, as published by the Free Software Foundation.              *
 * *
 * This program is distributed in the hope that it will be useful,       *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 * GNU Affero General Public License for more details.                   *
 * *
 * You should have received a copy of the GNU Affero General Public      *
 * License along with this program; if not, see                          *
 * <http://www.gnu.org/licenses/>.                                       *
 * ************************************************************************
 */
package lemmatizer.lucene.analysis.hebrew;

import lemmatizer.hebmorph.datastructures.DictHebMorph;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenStream;
import lemmatizer.lucene.analysis.hebrew.HebrewAnalyzer;
import lemmatizer.lucene.analysis.hebrew.HebrewTokenizer;
import lemmatizer.lucene.analysis.hebrew.TokenFilters.AddSuffixTokenFilter;
import lemmatizer.lucene.analysis.hebrew.TokenFilters.HebrewLemmatizerTokenFilter;
import lemmatizer.lucene.analysis.hebrew.TokenFilters.NiqqudFilter;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;

import java.io.IOException;

public class HebrewLegacyIndexingAnalyzer extends HebrewAnalyzer {
    public HebrewLegacyIndexingAnalyzer(DictHebMorph dict) {
        super(dict);
    }

    public HebrewLegacyIndexingAnalyzer() throws IOException {
        super();
    }

    @Override
    protected TokenStreamComponents createComponents(final String fieldName) {
        // on indexing we should always keep both the stem and marked original word
        // will ignore $ && will always output all lemmas + origin word$
        // basically, if analyzerType == AnalyzerType.INDEXING)
        lemmatizer.lucene.analysis.hebrew.HebrewTokenizer src = new HebrewTokenizer(dict.getPref(), SPECIAL_TOKENIZATION_CASES);
        src.setSuffixForExactMatch(originalTermSuffix);
        TokenStream tok = new NiqqudFilter(src);
        tok = new ASCIIFoldingFilter(tok);
        tok = new LowerCaseFilter(tok);
        tok = new HebrewLemmatizerTokenFilter(tok, dict);
        tok = new AddSuffixTokenFilter(tok, '$');
        return new TokenStreamComponents(src, tok);
    }

}
