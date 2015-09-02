package org.elasticsearch.index.analysis.uk;

import org.apache.lucene.analysis.TokenStream;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.analysis.AbstractTokenFilterFactory;
import org.elasticsearch.index.settings.IndexSettings;
import org.sotnya.lemmatizer.uk.engine.UkrainianLemmatizer;

import java.io.IOException;
import java.net.URISyntaxException;


public class UkrainianLemmatizerTokenFilterFactory extends AbstractTokenFilterFactory {
    @Inject
    public UkrainianLemmatizerTokenFilterFactory(Index index, @IndexSettings Settings indexSettings, String name, Settings settings) {
        super(index, indexSettings, name, settings);
    }

    @Override
    public TokenStream create(TokenStream tokenStream) {
        return new UkrainianLemmatizerTokenFilter(tokenStream, new UkrainianLemmatizer());
    }
}