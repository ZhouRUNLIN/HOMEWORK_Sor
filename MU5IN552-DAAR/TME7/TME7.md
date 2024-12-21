---
title: Module DAAR

---

# Module DAAR

## TME 7: Introduction to Elasticsearch - Data Aggregation and Visualization in Kibana

**_Prerequisite_**: Ensure Elastic and Kibana are installed and configured.

### **Exercise 1: Ingest Data into Elastic**

1.1 Ingest data into Elastic: Simple Flight Data
- Navigate to Kibana from the main menu > Try sample data > Other sample data sets.
- Add the "Simple Flight Data" dataset.

**Previous instructions (from TME6):**
- 启动两个docker镜像
- docker run --name es01 --net elastic -p 9200:9200 -p 9300:9300 -it docker.elastic.co/elasticsearch/elasticsearch:8.4.2
- docker run --name kib-01 --net elastic -p 5601:5601 docker.elastic.co/kibana/kibana:8.4.2

### **Exercise 2: Information Retrieval**

1. Using the visualizations, answer the following questions:

**Note**: *Choose the period: Last 1 week* 

- Total number of flights:

**Expected Response**:
```
2224 
```

- Percentage of delayed flights:

**Expected Response**:
```
25.2%
```

- Average most expensive ticket:

**Expected Response**:
```
$1199.70 
```

- Day with the highest number of flights:

**Expected Response**:
```
2022-10-01 
```

- Number of delayed flights with a delay greater than 360 minutes:

**Expected Response**:
```
23
```

2. Create the following visualizations:

- Connection between airports.
    - 不知道要我干啥
- Number of flights over time.
    - 0 : 2.8%
    - 8.691303887649621 : 0.4%
    - 14.327994258909735 : 0.4%
    - 11.54574953653565 : 0.4%
    - 14.474459358082783 : 0.2%
- Top 5 reasons for delays.
    - 先选择filter -> is not "No Delay"
    - Late Aircraft Delay : 28.0%
    - NAS Delay : 26.8%
    - Carrier Delay : 25.8%
    - Weather Delay : 10.6%
    - Security Delay : 8.8%
- Cities where delays are most prevalent.
    - Tokyo : 5.0%
    - Torino : 4.8%
    - Shanghai : 4.0%
    - Zurich : 4.0%
    - Xi'an : 4.0%


3. Combine all visualizations into a single dashboard and add the following filters:
- Origin City
- Destination City
- Average Ticket Price

### **Exercise 3: Information Retrieval**

Construct a dashboard from the 3 datasets summarizing the 3 dashboards.

![Response](image.png)

### **Exercise 4 (Bonus)**

Answer the questions from 2.1 using the Elastic API from dev tools.