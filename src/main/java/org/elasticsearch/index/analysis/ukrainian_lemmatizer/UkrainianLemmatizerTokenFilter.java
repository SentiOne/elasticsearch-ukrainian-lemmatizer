package org.elasticsearch.index.analysis.ukrainian_lemmatizer;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.KeywordAttribute;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import org.sotnya.lemmatizer.uk.engine.UkrainianLemmatizer;

public class UkrainianLemmatizerTokenFilter extends TokenFilter {
    private UkrainianLemmatizer lemmatizer = null;
    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
    private final KeywordAttribute keywordAttr = addAttribute(KeywordAttribute.class);

    public UkrainianLemmatizerTokenFilter(final TokenStream input, final UkrainianLemmatizer lemmatizer) {
        super(input);
        this.lemmatizer = lemmatizer;
    }

    /**
     * @return true if token was added to search/analysis stream
     * @throws IOException
     */
    @Override
    public final boolean incrementToken() throws IOException {
        if (!input.incrementToken()) {
            return false;
        }

        CharSequence lemma = lemmatizer.lemmatize(termAtt);

        if (lemma != null) {
            if(!keywordAttr.isKeyword() && !equalCharSequences(lemma, termAtt)) {
                termAtt.setEmpty().append(lemma);
            }
        }

        return true;
    }

    /**
     * Compare two char sequences for equality. Assumes non-null arguments.
     */
    private boolean equalCharSequences(CharSequence s1, CharSequence s2) {
        int len1 = s1.length();
        int len2 = s2.length();

        if (len1 != len2) return false;

        for (int i = len1; --i >= 0; ) {
            if (s1.charAt(i) != s2.charAt(i)) {
                return false;
            }
        }

        return true;
    }
}
