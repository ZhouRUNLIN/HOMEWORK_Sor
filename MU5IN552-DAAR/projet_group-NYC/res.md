---
title: result

---

# How To
1. Launch Elasticsearch with docker 
    ```
    docker run --name es01 --net elastic -p 9200:9200 -p 9300:9300 -t docker.elastic.co/elasticsearch/elasticsearch:8.4.3
    docker run --name kib-01 --net elastic -p 5601:5601 docker.elastic.co/kibana/kibana:8.4.3
    ```
2. ```
    docker cp es01:/usr/share/elasticsearch/config/certs/http_ca.crt .
    ```

## Response

### Q1: List all the neighborhoods in New York.
```json
GET /projet_nyc/_search
{
  "size": 0,
  "aggs": {
    "neighborhoods": {
      "terms": {
        "field": "NTA",
        "size": 10
      }
    }
  }
}
```

result(It's too long, only the first 10 will be displayed.):
```json
{
  "took": 7,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": {
      "value": 10000,
      "relation": "gte"
    },
    "max_score": null,
    "hits": []
  },
  "aggregations": {
    "neighborhoods": {
      "doc_count_error_upper_bound": 0,
      "sum_other_doc_count": 174560,
      "buckets": [
        {
          "key": "MN17",
          "doc_count": 13264
        },
        {
          "key": "MN13",
          "doc_count": 6916
        },
        {
          "key": "MN23",
          "doc_count": 6639
        },
        {
          "key": "MN24",
          "doc_count": 6581
        },
        {
          "key": "MN27",
          "doc_count": 5394
        },
        {
          "key": "MN22",
          "doc_count": 5191
        },
        {
          "key": "QN22",
          "doc_count": 5075
        },
        {
          "key": "MN15",
          "doc_count": 4500
        },
        {
          "key": "MN25",
          "doc_count": 4320
        },
        {
          "key": "MN19",
          "doc_count": 4179
        }
      ]
    }
  }
}
```

### Q2: Which neighborhood has the most restaurants?
```json
GET /projet_nyc/_search
{
  "size": 0,
  "aggs": {
    "top_neighborhood": {
      "terms": {
        "field": "NTA",
        "size": 1,
        "order": {
          "_count": "desc"
        }
      }
    }
  }
}
```
result
```json
{
  "took": 1,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": {
      "value": 10000,
      "relation": "gte"
    },
    "max_score": null,
    "hits": []
  },
  "aggregations": {
    "top_neighborhood": {
      "doc_count_error_upper_bound": 0,
      "sum_other_doc_count": 223355,
      "buckets": [
        {
          "key": "MN17",
          "doc_count": 13264
        }
      ]
    }
  }
}
```

### Q3: What does the violation code "04N" correspond to?
```json
GET /projet_nyc/_search
{
  "query": {
    "match": {
      "VIOLATION CODE": "04N"
    }
  },
  "_source": ["VIOLATION DESCRIPTION"],
  "size": 1
}
```

result
```json
{
  "took": 2,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": {
      "value": 10000,
      "relation": "gte"
    },
    "max_score": 3.0409007,
    "hits": [
      {
        "_index": "projet_nyc",
        "_id": "Cb4i2pIB98SNjIhih3eO",
        "_score": 3.0409007,
        "_source": {
          "VIOLATION DESCRIPTION": "Filth flies or food/refuse/sewage associated with (FRSA) flies or other nuisance pests in establishment��s food and/or non-food areas. FRSA flies include house flies, blow flies, bottle flies, flesh flies, drain flies, Phorid flies and fruit flies."
        }
      }
    ]
  }
}
```

### Q4: Where are the restaurants (name, address, neighborhood) that have a grade of A?
```json
GET /projet_nyc/_search
{
  "query": {
    "term": {
      "GRADE": "A"
    }
  },
  "_source": ["DBA", "BUILDING", "STREET", "ZIPCODE", "NTA"],
  "size": 10,
  "track_total_hits": true
}
```
result(There are 82,818 results, which is too long, so only the first 10 are displayed.)
```json
{
  "took": 3,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": {
      "value": 82818,
      "relation": "eq"
    },
    "max_score": 0.37006494,
    "hits": [
      {
        "_index": "projet_nyc",
        "_id": "074i2pIB98SNjIhih3WN",
        "_score": 0.37006494,
        "_source": {
          "DBA": "FRIEND RESTAURANT",
          "BUILDING": "11015",
          "ZIPCODE": 11419,
          "STREET": "101ST AVE",
          "NTA": "QN54"
        }
      },
      {
        "_index": "projet_nyc",
        "_id": "1r4i2pIB98SNjIhih3WN",
        "_score": 0.37006494,
        "_source": {
          "DBA": "ZEN VEGETARIAN HOUSE",
          "BUILDING": "773",
          "ZIPCODE": 11226,
          "STREET": "FLATBUSH AVENUE",
          "NTA": "BK60"
        }
      },
      {
        "_index": "projet_nyc",
        "_id": "2r4i2pIB98SNjIhih3WN",
        "_score": 0.37006494,
        "_source": {
          "DBA": "NEW HONG KONG CUISINE",
          "BUILDING": "10916",
          "ZIPCODE": 11433,
          "STREET": "MERRICK BLVD",
          "NTA": "QN01"
        }
      },
      {
        "_index": "projet_nyc",
        "_id": "3b4i2pIB98SNjIhih3WN",
        "_score": 0.37006494,
        "_source": {
          "DBA": "STAR PARK",
          "BUILDING": "6201",
          "ZIPCODE": 11204,
          "STREET": "20 AVENUE",
          "NTA": "BK28"
        }
      },
      {
        "_index": "projet_nyc",
        "_id": "5b4i2pIB98SNjIhih3WN",
        "_score": 0.37006494,
        "_source": {
          "DBA": "CRAB HOUSE",
          "BUILDING": "1223",
          "ZIPCODE": 11224,
          "STREET": "SURF AVENUE",
          "NTA": "BK23"
        }
      },
      {
        "_index": "projet_nyc",
        "_id": "674i2pIB98SNjIhih3WN",
        "_score": 0.37006494,
        "_source": {
          "DBA": "CARVE CAFE PIZZA",
          "BUILDING": "717",
          "ZIPCODE": 10036,
          "STREET": "8 AVENUE",
          "NTA": "MN15"
        }
      },
      {
        "_index": "projet_nyc",
        "_id": "7b4i2pIB98SNjIhih3WN",
        "_score": 0.37006494,
        "_source": {
          "DBA": "ORIGINAL JOE'S PIZZA",
          "BUILDING": "2136",
          "ZIPCODE": 10461,
          "STREET": "WILLIAMSBRIDGE ROAD",
          "NTA": "BX49"
        }
      },
      {
        "_index": "projet_nyc",
        "_id": "774i2pIB98SNjIhih3WN",
        "_score": 0.37006494,
        "_source": {
          "DBA": "1 QUETZAL",
          "BUILDING": "7210",
          "ZIPCODE": 11228,
          "STREET": "NEW UTRECHT AVENUE",
          "NTA": "BK28"
        }
      },
      {
        "_index": "projet_nyc",
        "_id": "9b4i2pIB98SNjIhih3WN",
        "_score": 0.37006494,
        "_source": {
          "DBA": "TRENTA TRE PIZZERIA",
          "BUILDING": "171",
          "ZIPCODE": 10016,
          "STREET": "MADISON AVENUE",
          "NTA": "MN17"
        }
      },
      {
        "_index": "projet_nyc",
        "_id": "-b4i2pIB98SNjIhih3WN",
        "_score": 0.37006494,
        "_source": {
          "DBA": "DIRTY PIERRES BISTRO",
          "BUILDING": "13",
          "ZIPCODE": 11375,
          "STREET": "STATION SQUARE",
          "NTA": "QN17"
        }
      }
    ]
  }
}
```

### Q5: What is the most popular cuisine? And by neighborhood?
To find the most popular cuisine overall:
```json
GET /projet_nyc/_search
{
  "size": 0,
  "aggs": {
    "top_cuisine": {
      "terms": {
        "field": "CUISINE DESCRIPTION.keyword",
        "size": 1,
        "order": {
          "_count": "desc"
        }
      }
    }
  }
}
```
result
```json
{
  "took": 1,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": {
      "value": 10000,
      "relation": "gte"
    },
    "max_score": null,
    "hits": []
  },
  "aggregations": {
    "top_cuisine": {
      "doc_count_error_upper_bound": 0,
      "sum_other_doc_count": 198402,
      "buckets": [
        {
          "key": "American",
          "doc_count": 38217
        }
      ]
    }
  }
}
```

To find the most popular cuisine by neighborhood:
```json
GET /projet_nyc/_search
{
  "size": 0,
  "aggs": {
    "neighborhoods": {
      "terms": {
        "field": "NTA.keyword",
        "size": 1000
      },
      "aggs": {
        "top_cuisine": {
          "terms": {
            "field": "CUISINE DESCRIPTION.keyword",
            "size": 1,
            "order": {
              "_count": "desc"
            }
          }
        }
      }
    }
  }
}
```

result(The result is too long, so only the first 10 neighborhoods are displayed.)
```json
{
  "took": 85,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": {
      "value": 10000,
      "relation": "gte"
    },
    "max_score": null,
    "hits": []
  },
  "aggregations": {
    "neighborhoods": {
      "doc_count_error_upper_bound": 0,
      "sum_other_doc_count": 174560,
      "buckets": [
        {
          "key": "MN17",
          "doc_count": 13264,
          "top_cuisine": {
            "doc_count_error_upper_bound": 0,
            "sum_other_doc_count": 9243,
            "buckets": [
              {
                "key": "American",
                "doc_count": 4021
              }
            ]
          }
        },
        {
          "key": "MN13",
          "doc_count": 6916,
          "top_cuisine": {
            "doc_count_error_upper_bound": 0,
            "sum_other_doc_count": 5101,
            "buckets": [
              {
                "key": "American",
                "doc_count": 1815
              }
            ]
          }
        },
        {
          "key": "MN23",
          "doc_count": 6639,
          "top_cuisine": {
            "doc_count_error_upper_bound": 0,
            "sum_other_doc_count": 5077,
            "buckets": [
              {
                "key": "American",
                "doc_count": 1562
              }
            ]
          }
        },
        {
          "key": "MN24",
          "doc_count": 6581,
          "top_cuisine": {
            "doc_count_error_upper_bound": 0,
            "sum_other_doc_count": 5339,
            "buckets": [
              {
                "key": "American",
                "doc_count": 1242
              }
            ]
          }
        },
        {
          "key": "MN27",
          "doc_count": 5394,
          "top_cuisine": {
            "doc_count_error_upper_bound": 0,
            "sum_other_doc_count": 3753,
            "buckets": [
              {
                "key": "Chinese",
                "doc_count": 1641
              }
            ]
          }
        },
        {
          "key": "MN22",
          "doc_count": 5191,
          "top_cuisine": {
            "doc_count_error_upper_bound": 0,
            "sum_other_doc_count": 4300,
            "buckets": [
              {
                "key": "American",
                "doc_count": 891
              }
            ]
          }
        },
        {
          "key": "QN22",
          "doc_count": 5075,
          "top_cuisine": {
            "doc_count_error_upper_bound": 0,
            "sum_other_doc_count": 2138,
            "buckets": [
              {
                "key": "Chinese",
                "doc_count": 2937
              }
            ]
          }
        },
        {
          "key": "MN15",
          "doc_count": 4500,
          "top_cuisine": {
            "doc_count_error_upper_bound": 0,
            "sum_other_doc_count": 3551,
            "buckets": [
              {
                "key": "American",
                "doc_count": 949
              }
            ]
          }
        },
        {
          "key": "MN25",
          "doc_count": 4320,
          "top_cuisine": {
            "doc_count_error_upper_bound": 0,
            "sum_other_doc_count": 2992,
            "buckets": [
              {
                "key": "American",
                "doc_count": 1328
              }
            ]
          }
        },
        {
          "key": "MN19",
          "doc_count": 4179,
          "top_cuisine": {
            "doc_count_error_upper_bound": 0,
            "sum_other_doc_count": 3220,
            "buckets": [
              {
                "key": "American",
                "doc_count": 959
              }
            ]
          }
        }
      ]
    }
  }
}
```

### Q6: What is the date of the last inspection?
```json
GET /projet_nyc/_search
{
  "size": 1,
  "sort": [
    {
      "INSPECTION DATE": {
        "order": "desc"
      }
    }
  ],
  "_source": ["INSPECTION DATE"]
}
```

result
```json
{
  "took": 5,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": {
      "value": 10000,
      "relation": "gte"
    },
    "max_score": null,
    "hits": [
      {
        "_index": "projet_nyc",
        "_id": "Zr4i2pIB98SNjIhilJ0G",
        "_score": null,
        "_source": {
          "INSPECTION DATE": "10/26/2024"
        },
        "sort": [
          1729900800000
        ]
      }
    ]
  }
}
```

### Q7: Provide a list of Chinese restaurants with an A grade in Brooklyn.
```json
GET /projet_nyc/_search
{
  "query": {
    "bool": {
      "must": [
        { "match": { "GRADE": "A" }},
        { "match": { "CUISINE DESCRIPTION": "Chinese" }},
        { "match": { "BORO": "Brooklyn" }}
      ]
    }
  },
  "_source": ["DBA", "BUILDING", "STREET", "ZIPCODE", "NTA"],
  "size": 1000
}
```

result(There are 1955 results, which is too long, so only the top 10 Chinese restaurants are displayed.)
```json
{
  "took": 8,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": {
      "value": 1955,
      "relation": "eq"
    },
    "max_score": 3.9846635,
    "hits": [
      {
        "_index": "projet_nyc",
        "_id": "3b4i2pIB98SNjIhih3WN",
        "_score": 3.9846635,
        "_source": {
          "DBA": "STAR PARK",
          "BUILDING": "6201",
          "ZIPCODE": 11204,
          "STREET": "20 AVENUE",
          "NTA": "BK28"
        }
      },
      {
        "_index": "projet_nyc",
        "_id": "Lr4i2pIB98SNjIhih3aN",
        "_score": 3.9846635,
        "_source": {
          "DBA": "MOON & FLOWER HOUSE",
          "BUILDING": "5912",
          "ZIPCODE": 11220,
          "STREET": "7 AVENUE",
          "NTA": "BK34"
        }
      },
      {
        "_index": "projet_nyc",
        "_id": "tr4i2pIB98SNjIhih3aO",
        "_score": 3.9846635,
        "_source": {
          "DBA": "FU LAI",
          "BUILDING": "7023",
          "ZIPCODE": 11228,
          "STREET": "FORT HAMILTON PARKWAY",
          "NTA": "BK30"
        }
      },
      {
        "_index": "projet_nyc",
        "_id": "LL4i2pIB98SNjIhih3eO",
        "_score": 3.9846635,
        "_source": {
          "DBA": "LUCKY KITCHEN",
          "BUILDING": "227",
          "ZIPCODE": 11213,
          "STREET": "ALBANY AVENUE",
          "NTA": "BK61"
        }
      },
      {
        "_index": "projet_nyc",
        "_id": "Pb4i2pIB98SNjIhih3eO",
        "_score": 3.9846635,
        "_source": {
          "DBA": "GOOD FRIENDS RESTAURANT",
          "BUILDING": "9528",
          "ZIPCODE": 11236,
          "STREET": "AVENUE L",
          "NTA": "BK50"
        }
      },
      {
        "_index": "projet_nyc",
        "_id": "qb4i2pIB98SNjIhih3eO",
        "_score": 3.9846635,
        "_source": {
          "DBA": "CHINA NO. 1 CHINESE RESTAURANT",
          "BUILDING": "9602",
          "ZIPCODE": 11212,
          "STREET": "CHURCH AVENUE",
          "NTA": "BK96"
        }
      },
      {
        "_index": "projet_nyc",
        "_id": "874i2pIB98SNjIhih3eO",
        "_score": 3.9846635,
        "_source": {
          "DBA": "ORIENTAL PALACE H.K. KITCHEN",
          "BUILDING": "3018",
          "ZIPCODE": 11224,
          "STREET": "MERMAID AVENUE",
          "NTA": "BK21"
        }
      },
      {
        "_index": "projet_nyc",
        "_id": "BL4i2pIB98SNjIhih3iO",
        "_score": 3.9846635,
        "_source": {
          "DBA": "KAM LONG KITCHEN KINGS",
          "BUILDING": "1031",
          "ZIPCODE": 11212,
          "STREET": "RUTLAND ROAD",
          "NTA": "BK96"
        }
      },
      {
        "_index": "projet_nyc",
        "_id": "M74i2pIB98SNjIhih3iO",
        "_score": 3.9846635,
        "_source": {
          "DBA": "GOOD FRIENDS RESTAURANT",
          "BUILDING": "9528",
          "ZIPCODE": 11236,
          "STREET": "AVENUE L",
          "NTA": "BK50"
        }
      },
      {
        "_index": "projet_nyc",
        "_id": "Vr4i2pIB98SNjIhih3iO",
        "_score": 3.9846635,
        "_source": {
          "DBA": "RICH VILLAGE RESTAURANT",
          "BUILDING": "6009",
          "ZIPCODE": 11220,
          "STREET": "7 AVENUE",
          "NTA": "BK34"
        }
      }
    ]
  }
}
```

### Q8: What is the address of the restaurant LADUREE?
```json
GET /projet_nyc/_search
{
  "query": {
    "match": {
      "DBA": "LADUREE"
    }
  },
  "_source": ["BUILDING", "STREET", "ZIPCODE", "NTA"],
  "size": 1
}
```

result
```json
{
  "took": 8,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": {
      "value": 34,
      "relation": "eq"
    },
    "max_score": 11.812864,
    "hits": [
      {
        "_index": "projet_nyc",
        "_id": "qb4i2pIB98SNjIhiqOE-",
        "_score": 11.812864,
        "_source": {
          "BUILDING": "864",
          "ZIPCODE": 10021,
          "STREET": "MADISON AVENUE",
          "NTA": "MN40"
        }
      }
    ]
  }
}
```


### Q9: What cuisine is most affected by the violation "Hot food item not held at or above 140º F"?
```json
GET /projet_nyc/_search
{
  "query": {
    "match": {
      "VIOLATION DESCRIPTION": "Hot food item not held at or above 140º F"
    }
  },
  "size": 0,
  "aggs": {
    "top_cuisine": {
      "terms": {
        "field": "CUISINE DESCRIPTION.keyword",
        "size": 1,
        "order": {
          "_count": "desc"
        }
      }
    }
  }
}
```

result
```json
{
  "took": 30,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": {
      "value": 10000,
      "relation": "gte"
    },
    "max_score": null,
    "hits": []
  },
  "aggregations": {
    "top_cuisine": {
      "doc_count_error_upper_bound": 0,
      "sum_other_doc_count": 192036,
      "buckets": [
        {
          "key": "American",
          "doc_count": 37007
        }
      ]
    }
  }
}
```

## For Q10 and Q11, the existing index projet_nyc lacks the necessary field mappings, so we need to recreate the index, add the necessary field mappings, and reimport the data.


### Q10: What are the most common violations (Top 5)?
```json
POST /new_projet_nyc/_search
{
  "size": 0,
  "aggs": {
    "top_violations": {
      "terms": {
        "field": "VIOLATION DESCRIPTION.keyword",
        "size": 5,
        "order": {
          "_count": "desc"
        }
      }
    }
  }
}
```
result
```json
{
  "took": 1,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": {
      "value": 10000,
      "relation": "gte"
    },
    "max_score": null,
    "hits": []
  },
  "aggregations": {
    "top_violations": {
      "doc_count_error_upper_bound": 0,
      "sum_other_doc_count": 151048,
      "buckets": [
        {
          "key": "Non-food contact surface or equipment made of unacceptable material, not kept clean, or not properly sealed, raised, spaced or movable to allow accessibility for cleaning on all sides, above and underneath the unit.",
          "doc_count": 26357
        },
        {
          "key": "Establishment is not free of harborage or conditions conducive to rodents, insects or other pests.",
          "doc_count": 18933
        },
        {
          "key": "Food contact surface not properly washed, rinsed and sanitized after each use and following any activity when contamination may have occurred.",
          "doc_count": 16601
        },
        {
          "key": "Cold TCS food item held above 41 ��F; smoked or processed fish held above 38 ��F; intact raw eggs held above 45 ��F; or reduced oxygen packaged (ROP) TCS foods held above required temperatures except during active necessary preparation.",
          "doc_count": 12004
        },
        {
          "key": "Anti-siphonage or back-flow prevention device not provided where required; equipment or floor not properly drained; sewage disposal system in disrepair or not functioning properly. Condensation or liquid waste improperly disposed of.",
          "doc_count": 11676
        }
      ]
    }
  }
}
```
### Q11: What is the most popular restaurant chain?
```json
GET /projet_nyc/_search
{
  "size": 0,
  "aggs": {
    "top_restaurant_chain": {
      "terms": {
        "field": "DBA.keyword",
        "size": 1,
        "order": {
          "_count": "desc"
        }
      }
    }
  }
}
```

result
```json
{
  "took": 41,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": {
      "value": 10000,
      "relation": "gte"
    },
    "max_score": null,
    "hits": []
  },
  "aggregations": {
    "most_popular_chain": {
      "doc_count_error_upper_bound": 0,
      "sum_other_doc_count": 233663,
      "buckets": [
        {
          "key": "DUNKIN",
          "doc_count": 2956
        }
      ]
    }
  }
}
```

