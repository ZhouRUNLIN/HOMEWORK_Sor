---
title: How To

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
        "field": "NTA.keyword",
        "size": 10000
      }
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
        "field": "NTA.keyword",
        "size": 1,
        "order": {
          "_count": "desc"
        }
      }
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

### Q4: Where are the restaurants (name, address, neighborhood) that have a grade of A?
```json
GET /projet_nyc/_search
{
  "query": {
    "match": {
      "GRADE": "A"
    }
  },
  "_source": ["DBA", "BUILDING", "STREET", "ZIPCODE", "NTA"],
  "size": 1000
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

### Q10: What are the most common violations (Top 5)?
```json
GET /projet_nyc/_search
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

