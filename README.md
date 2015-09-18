# Ukrainian lemmatizer plugin for ElasticSearch

The plugin provides a capability to search across documents, written in ukrainian, using words in different forms. 

## Principles

The thing is, it makes you able to index not the source's words but their lemmas (lemma – canonical form of word) and also perform a lookup using different forms of the same word which will return you what you're looking for. Needless to say, the magic is being done under the hood! No more doubts like: "What if I put this word in plural? Maybe it'll finally find something?".
Each term before settling in the storage will be passed through ```UkrainianAnalyzer``` which looks in ```UkrainianLemmatizer``` if it has a lemma for the term and, in case of success, this lemma must get into index. The same sequence of actions has the place when you start a lookup over documents stored using the analyzer: it will convert your search terms according to dictionary and return results if there is any match.

## Installation

Installation of the plugin consists of only 4 steps:

 * Clone this repository
 * Get inside the root dir of cloned repo and run ```gradle release```
 * Find built artifact in ```build/distributions/```
 * Import it into your ES installation with ```<pathh_to_es_bin_dir>/plugin --url <path_to_distribution>/elasticsearch-ukrainian-lemmatizer-1.0-SNAPSHOT.zip --install ukrainian-lemmatizer```


## Usage

Here are simple examples of plugin usage which rely on ES HTTP API.
First of all we need to create the index which must include our analyzer:

```bash
# Create index with settings
curl -XPUT "http://localhost:9200/ukrainian/" -d '
{
   "settings":{
      "index":{
         "analysis":{
            "analyzer":{
               "ukrainian":{
                  "type": "ukrainian"
               }
            }
         }
      }
   }
}
'
```

Having that done and filled this index with some data we may query it using the same analyzer:

```bash
# Search
curl -XPOST "http://localhost:9200/ukrainian/user/_search?pretty=true" -d '
{
   "query":{
      "match":{
         "_all": {
             "query": "гусятах",
             "analyzer": "ukrainian"
         }
      }
   }
}
'
```

**Notice** you may find complete example in ```test.sh``` inside the repository: you may use it for testing of serviceability of the plugin after you install it.

## Requirements

* ES 1.7+
* Java 8
* Gradle