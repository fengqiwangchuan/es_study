```
GET _search
{
  "query": {
    "match_all": {}
  }
}

POST _analyze
{
  "analyzer": "ik_max_word",
  "text": ["指定id修改数据"]
}

PUT wp
{
  "settings": {
    "number_of_replicas": 0,
    "number_of_shards": 1
  }
}

GET wp
HEAD wp2
GET *
DELETE wp2

PUT wp/_mapping/goods
{
  "properties": {
    "title": {
      "type": "text",
      "analyzer": "ik_max_word"
    },
    "images": {
      "type": "keyword",
      "index": false
    },
    "price": {
      "type": "float"
    }
  }
}

GET wp/_mapping
GET wp/goods
{
  "title": "wp"
}

POST wp/goods
{
  "title": "wp"
}
POST wp/goods/WNayqW8B7cai4d9FW1ON
{
  "title": "wp",
  "price": 2699
}
GET _search
{
  "query": {
    "match_all": {}
  }
}
POST wp/goods/2
{
  "title": "指定id",
  "price": "12345"
}
POST wp/goods/3
{
  "title": "动态添加mapping映射",
  "price": 2699,
  "stock": 200,
  "saleable": true
}
POST wp/goods/4
{
  "title": "原数据"
}
POST wp/goods/5
{
  "title": "原数据"
}
DELETE wp/goods/5
PUT wp/goods/4
{
  "title": "指定id修改数据",
  "price": 3333.00
}
GET wp/_search
{
  "query": {
    "match": {
      "title": {
        "query": "指定修改数据",
        "operator": "and",
        "minimum_should_match": "20%"
      }
    }
  }
}
GET wp/_search
{
  "query": {
    "multi_match": {
      "query": "wp",
      "fields": ["title","subTitle"]
    }
  }
}
GET wp/_search/
{
  "query": {
    "term": {
        "price": "2699"
    }
  }
}
GET wp/_search
{
  "query": {
    "terms": {
      "price": [
        "2699",
        "3333"
      ]
    }
  }
}
GET wp/_search
{
  "_source": ["price"], 
  "query": {
    "term": {
      "price": 2699
    }
  }
}
GET wp/_search
{
  "_source": {
    "include": ["title","stock"]
  }, 
  "query": {
    "term": {
      "price": 2699
    }
  }
}
GET wp/_search
{
  "_source": {
    "excludes": ["price"]
  },
  "query": {
    "term": {
      "price": 2699
    }
  }
}
GET wp/_search
{
  "query": {
    "bool": {
      "must": [
        {"match": {
          "title": "添加"
        }}
      ],
      "must_not": [
        {"match": {
          "title": "动态"
        }}
      ],
      "should": [
        {"match": {
          "title": "动态"
        }}
      ]
    }
  }
}
GET wp/_search
{
  "query": {
    "range": {
      "price": {
        "gte": 10,
        "lte": 2999
      }
    }
  }
}
POST wp/goods/6
{
  "title": "apple学生电脑",
  "price": 6666.66,
  "stock": 1
}
GET wp/_search
{
  "query": {
    "fuzzy": {
      "title": "appla"
    }
  }
}
GET wp/_search
{
  "query": {
    "fuzzy": {
      "title": {
        "value": "appla",
        "fuzziness": 1
      }
    }
  }
}
GET wp/_search
{
  "query": {
    "bool": {
      "must": [
        {"match": {
          "title": "apple"
        }}
      ],
      "filter": {
        "range": {
          "price": {
            "gte": 10,
            "lte": 7777
          }
        }
      }
    }
  }
}
GET wp/_search
{
  "query": {
    "constant_score": {
      "filter": {
        "range": {
          "price": {
            "gte": 2666,
            "lte": 2999
          }
        }
      },
      "boost": 1.2
    }
  }
}
GET wp/_search
{
  "query": {
    "match": {
      "title": "指定"
    }
  },
  "sort": [
    {
      "price": {
        "order": "desc"
      }
    }
  ]
}
GET wp/goods/_search
{
  "query": {
    "bool": {
      "must": [
        {"match": {
          "title": "指定"
        }}
      ]
    }
  },
  "sort": [
    {
      "price": {
        "order": "desc"
      }
    },
    {
      "_score": {
        "order": "desc"
      }
    }
  ]
}

PUT /cars
{
  "settings": {
    "number_of_shards": 1,
    "number_of_replicas": 0
  },
  "mappings": {
    "transactions": {
      "properties": {
        "color": {
          "type": "keyword"
        },
        "make": {
          "type": "keyword"
        }
      }
    }
  }
}
POST /cars/transactions/_bulk
{ "index": {}}
{ "price" : 10000, "color" : "red", "make" : "honda", "sold" : "2014-10-28" }
{ "index": {}}
{ "price" : 20000, "color" : "red", "make" : "honda", "sold" : "2014-11-05" }
{ "index": {}}
{ "price" : 30000, "color" : "green", "make" : "ford", "sold" : "2014-05-18" }
{ "index": {}}
{ "price" : 15000, "color" : "blue", "make" : "toyota", "sold" : "2014-07-02" }
{ "index": {}}
{ "price" : 12000, "color" : "green", "make" : "toyota", "sold" : "2014-08-19" }
{ "index": {}}
{ "price" : 20000, "color" : "red", "make" : "honda", "sold" : "2014-11-05" }
{ "index": {}}
{ "price" : 80000, "color" : "red", "make" : "bmw", "sold" : "2014-01-01" }
{ "index": {}}
{ "price" : 25000, "color" : "blue", "make" : "ford", "sold" : "2014-02-12" }

GET /cars/_search
{
  "size": 0,
  "aggs": {
    "popular_colors": {
      "terms": {
        "field": "color",
        "size": 10
      }
    }
  }
}

GET cars/_search
{
  "size": 0,
  "aggs": {
    "popular_colors": {
      "terms": {
        "field": "color",
        "size": 10
      },
      "aggs": {
        "avg_price": {
          "avg": {
            "field": "price"
          }
        }
      }
    }
  }
}

GET cars/_search
{
  "size": 0,
  "aggs": {
    "popular_colors": {
      "terms": {
        "field": "color",
        "size": 10
      },
      "aggs": {
        "avg_price": {
          "avg": {
            "field": "price"
          }
        },
        "maker": {
          "terms": {
            "field": "make",
            "size": 10
          }
        }
      }
    }
  }
}

GET cars/_search
{
  "size": 0,
  "aggs": {
    "price": {
      "histogram": {
        "field": "price",
        "interval": 5000,
        "min_doc_count": 1
      }
    }
  }
}
```