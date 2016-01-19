package org.sotnya.lemmatizer.uk.engine;

import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.*;
import java.lang.RuntimeException;
import java.lang.String;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to serve purposes of term-to-lemma substitution.
 * Handles mapping retrieval, terms normalisation and lookup for proper lemmas in mapping.
 */
public class UkrainianLemmatizer {
    private static final Map<String, String> dictionary;
    /**
     * Before the lookup we replace some symbols to their alternatives are being used in mapping.
     * Right now there is the single substitution case: ukrainian apostrophes with to english single quote.
     */
    private static final Map<Character, Character> replaceItems = new HashMap<Character, Character>(2) {{
        put('’', '\'');
        put('ʼ', '\'');
    }};

    static {
        // load mapping from file (must be changed to faster and memory-efficient type)
        final InputStream is = UkrainianLemmatizer.class.getClassLoader().getResourceAsStream("mapping_sorted.csv");
        final String separator = ",";

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            dictionary = new HashMap<String, String>();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] splittedLine = line.split(separator);
                dictionary.put(splittedLine[0], splittedLine[1]);
            }
        } catch (IOException e) {
            throw new RuntimeException("IO error while loading mapping_sorted.csv", e);
        }

        // Let's wait some additional second but we'll keep our heap clean from
        // lots of short-lived objects created during the loading.
        System.gc();
    }

    /**
     * @param termAtt Token (word) we should compare with existing in our dictionary.
     *
     * @return CharSequence value which is not null in case if we have related lemma in dict.
     */
    public CharSequence lemmatize(CharTermAttribute termAtt) {
        String term = termAtt.toString();

        for (Map.Entry<Character, Character> e : replaceItems.entrySet()) {
            term = term.replace(e.getKey(), e.getValue());
        }

        return dictionary.containsKey(term) ? dictionary.get(term) : null;
    }
}
