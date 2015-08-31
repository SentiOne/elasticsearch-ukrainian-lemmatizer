package org.elasticsearch.indices.analysis.uk;

import org.apache.lucene.analysis.TokenStream;
import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.analysis.AnalyzerScope;
import org.elasticsearch.index.analysis.PreBuiltAnalyzerProviderFactory;
import org.elasticsearch.index.analysis.PreBuiltTokenFilterFactoryFactory;
import org.elasticsearch.index.analysis.TokenFilterFactory;
import org.elasticsearch.index.analysis.uk.UkrainianLemmagenTokenFilter;
import org.elasticsearch.indices.analysis.IndicesAnalysisService;
import org.sotnya.lemmagen.uk.engine.UkrainianLemmagen;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Registers indices level analysis components so, if not explicitly configured, will be shared
 * among all indices.
 */
public class UkrainianIndicesAnalysis extends AbstractComponent {

    @Inject
    public UkrainianIndicesAnalysis(Settings settings, IndicesAnalysisService indicesAnalysisService) {
        super(settings);

        indicesAnalysisService.analyzerProviderFactories().put("ukrainian", new PreBuiltAnalyzerProviderFactory("ukrainian", AnalyzerScope.INDICES, new UkrainianAnalyzer()));

        indicesAnalysisService.tokenFilterFactories().put("ukrainian_lemmagen", new PreBuiltTokenFilterFactoryFactory(new TokenFilterFactory() {
            @Override
            public String name() {
                return "ukrainian_lemmagen";
            }

            UkrainianLemmagen lemmatizer;

            @Override
            public TokenStream create(TokenStream tokenStream) {
                try {
                    lemmatizer = new UkrainianLemmagen();
                } catch (IOException | URISyntaxException ex) {
                    throw new RuntimeException("Unable to load default mapping tables", ex);
                }

                return new UkrainianLemmagenTokenFilter(tokenStream, lemmatizer);
            }
        }));
    }
}