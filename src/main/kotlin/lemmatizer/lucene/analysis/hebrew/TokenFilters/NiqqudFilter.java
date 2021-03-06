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
package lemmatizer.lucene.analysis.hebrew.TokenFilters;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;


public final class NiqqudFilter extends TokenFilter {
    public NiqqudFilter(TokenStream input) {
        super(input);
    }

    private CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);

    @Override
    public final boolean incrementToken() throws IOException {
        if (!input.incrementToken()) { // reached EOS -- return null
            return false;
        }

        char[] buffer = termAtt.buffer();
        final int length = termAtt.length();
        int j = 0;
        for (int i = 0; i < length; i++) {
            if ((buffer[i] < 1455) || (buffer[i] > 1476)) { // current position is not a Niqqud character
                buffer[j++] = buffer[i];
            }
        }
        termAtt.setLength(j);
        return true;
    }
}