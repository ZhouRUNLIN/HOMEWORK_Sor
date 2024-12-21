---
title: '**DAAR Module**'

---

# **DAAR Module**
## **TME 6: Introduction to Elasticsearch - Indexing and Searching**

### **Exercise 1: Install Elasticsearch**
Download and install Elasticsearch

- Create a network
- Retrieve the Elasticsearch image
- Launch the image

**Answer:**

```
docker network create elastic
docker pull docker.elastic.co/elasticsearch/elasticsearch:8.4.2
docker run --name es01 --net elastic -p 9200:9200 -p 9300:9300 -it docker.elastic.co/elasticsearch/elasticsearch:8.4.2
```
**Note**: When you start Elasticsearch for the first time, the generated elastic user password and Kibana enrollment token are displayed in the terminal.

本地的信息在info.txt里

### **Exercise 2: Test the Configuration**
2.1. Verify that the instance is up from the browser or via curl or postman: http://127.0.0.1:9200

需要切换至https进行访问，使用命令：
curl --cacert http_ca.crt -u elastic https://localhost:9200

**Expected Answer:**

```
{
  "name" : "elasticsearch-fd9946598-4lnrm",
  "cluster_name" : "docker-| ",
  ...
  "tagline" : "You Know, for Search"
}
```

2.2. To view the nodes, using the CAT API (Compact And Aligned Text):
- Display the health status of the | : 
http://127.0.0.1:9200/_cat/health?v 
**Expected Answer:**


| epoch | timestamp | cluster | status | node.total | node.data | shards | pri | relo | init | unassign | pending_tasks | max_task_wait_time | active_shards_percent|
|------------|-----------|----------------|--------|------------|-----------|--------|-----|------|------|----------|---------------|--------------------|-------------------|
| 1730018982 | 08:49:42  | docker-cluster | green  | 1          | 1         | 2      | 2   | 0    | 0    | 0| 0 | - | 100% 


2.3. Test the cluster with Kibana

- Retrieve the Elasticsearch image
- Launch the image
- Launch: http://localhost:5601

**Answer:**

```
docker pull docker.elastic.co/kibana/kibana:8.4.2
docker run --name kib-01 --net elastic -p 5601:5601 docker.elastic.co/kibana/kibana:8.4.2
```
**Note**: Use the dev console in Kibana

2.4 Query: See the version and information of the launched Elastic instance

GET /

**Expected Answer**: 
```
{
  "name": "e81a28daaac3",
  "cluster_name": "docker-cluster",
  "cluster_uuid": "4R_Vo-bJT3Cwo4MbeOe1JQ",
  "version": {
    "number": "8.4.2",
    "build_flavor": "default",
    "build_type": "docker",
    "build_hash": "89f8c6d8429db93b816403ee75e5c270b43a940a",
    "build_date": "2022-09-14T16:26:04.382547801Z",
    "build_snapshot": false,
    "lucene_version": "9.3.0",
    "minimum_wire_compatibility_version": "7.17.0",
    "minimum_index_compatibility_version": "7.0.0"
  },
  "tagline": "You Know, for Search"
}
```

2.5 Query: View the nodes using the CAT API (Compact And Aligned Text)

GET _cat/nodes
```
172.18.0.2 18 97 1 1.43 1.27 0.92 cdfhilmrstw * e81a28daaac3
```

2.6 Query: A variant of question 2.5 by adding the v argument for verbose

GET _cat/nodes?v

**Expected Answer**:
```
ip         heap.percent ram.percent cpu load_1m load_5m load_15m node.role   master name
172.18.0.2           21          97   1    1.61    1.33     0.95 cdfhilmrstw *      e81a28daaac3
```
**Note**: What interests us, the columns node.role => each letter has a meaning, see the definition of each role, then the master column has the value * to indicate that our node is a master.

See documentation: [Elasticsearch Node Module](https://www.elastic.co/guide/en/elasticsearch/reference/current/modules-node.html)

- 节点信息表：该命令返回一个包含各节点信息的表格，表中会显示节点的详细属性。使用 v 参数，表头会显示每个字段的含义。
- node.role: 显示节点的角色，每个字母代表不同的角色，如 m 表示主节点（master）、d 表示数据节点（data）、c 表示协调节点（coordinating）。
- master: 表示该节点是否是主节点。如果列值为 *，则表明当前节点是主节点。

**the node is assigned the following roles:**

1. master
2. data
3. data_content
4. data_hot
5. data_warm
6. data_cold
7. data_frozen
8. ingest
9. ml
10. remote_cluster_client
11. transform


2.7 To go further: modify the API response display:

**Example**: Run the following command in DevTools:

GET /_cat/nodes?v&h=name,ip,diskUsed,diskAvail,diskUsedPercent

**Note**: This shows the node's internal IP, the disk usage rate (diskUsed), available hard disk space (diskAvail), and the percentage of disk used (diskUsedPercent).

See documentation: [CAT Nodes API](https://www.elastic.co/guide/en/elasticsearch/reference/current/cat-nodes.html#cat-nodes-api-query-params)

```
name         ip         diskUsed diskAvail diskUsedPercent
e81a28daaac3 172.18.0.2    9.5gb    48.7gb           16.42
```

### **Exercise 3: Add Data to Elasticsearch**
**Note**: Means to interact with Elastic (Add, Update, Read, 鈥�)

- Elasticsearch API (Python, Java, etc.)
- With the Elasticsearch API itself (what we'll see in the TME)
- With beats (metricbeat, filebeat, etc.)
- On the Kibana screen using dev tools or the "Upload Data鈥� option by directly loading from a file

3.1. Go to Kibana, choose Dev Tools>Console.

3.2. Add the name of a person John Doe in the customer index

**Expected Answer**:

```
POST /customer/_doc/1
{
  "name": "John Doe"
}
```

Respond : 
```
{
  "_index": "customer",
  "_id": "1",
  "_version": 1,
  "result": "created",
  "_shards": {
    "total": 2,
    "successful": 1,
    "failed": 0
  },
  "_seq_no": 0,
  "_primary_term": 1
}
```

3.3. Verify the addition

**Expected Answer**:

GET /customer/_doc/1

**Note**: To add multiple documents at the same time, we will use the _bulk query

3.4. Add the following table:

Data

**Expected Answer**:


See documentation: [Getting Started with Elasticsearch](https://www.elastic.co/guide/en/welcome-to-elastic/current/getting-started-general-purpose.html#gp-gs-add-data)

3.5 **Bonus Questions**:
- Find the youngest customer
    ```
        GET /customer/_search
        {
          "size": 1,
          "sort": [
            { "age": "asc" }
          ],
          "_source": ["name", "age"]
        }
    ```
- Find the highest balance
    ```
    GET /customer/_search
    {
      "size": 1,
      "sort": [
        { "balance": "desc" }
      ],
      "_source": ["name", "balance"]
    }
    ```

- Find the average age of the customers
    ```
    GET /customer/_search
    {
      "size": 0,
      "aggs": {
        "average_age": {
          "avg": {
            "field": "age"
          }
        }
      }
    }
    ```

- Find the number of women
    ```
    GET /customer/_search
    {
      "size": 0,
      "query": {
        "term": {
          "gender": "female"
        }
      }
    }
    ```
    
