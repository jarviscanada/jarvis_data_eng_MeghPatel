# Introduction
This project demonstrates a complete **retail data analytics pipeline** implemented across both **Databricks** and **Apache Zeppelin** using **PySpark, Hadoop, PostgreSQL, and Hive**.

The goal of this project is to help project team members understand the **design, architecture, feature engineering, and business analytics workflow** by reviewing the README and notebooks.

The solution focuses on scalable big data analytics using:
- **PostgreSQL Docker** as transactional data source
- **CSV-based retail dataset ingestion**
- **PySpark ETL and wrangling**
- **RFM customer segmentation**
- **Revenue and country-level analysis**
- **Dual-platform implementation**
  - Databricks (cloud analytics)
  - Zeppelin + Hadoop on GCP VM

---

# Architecture Overview
The project implements the same retail analytics workflow across **two big data environments**:

- **Databricks on Azure**
  - JDBC ingestion from PostgreSQL
  - DBFS managed storage
  - PySpark transformations
  - Hive Metastore
  - notebook visualizations

- **Zeppelin on GCP VM**
  - JDBC ingestion from PostgreSQL Docker
  - HDFS storage
  - Spark on Hadoop
  - Hive Metastore
  - `z.show()` visualizations

### Architecture Diagram
![Databricks and Zeppelin Architecture](./assets/databricks_zeppelin_architecture.png)

---

# Databricks and Hadoop Implementation

## Dataset and Analytics Work
The project uses the **Online Retail dataset**, which contains:
- Invoice number
- Product description
- Quantity
- Unit price
- Customer ID
- Country
- Invoice timestamp

### Analytics Performed
- JDBC ingestion from PostgreSQL
- CSV ingestion into DBFS
- Data cleaning and null handling
- Revenue feature engineering
- Product and country trend analysis
- RFM customer segmentation
- Spark SQL + DataFrame transformations
- Notebook visualizations

Notebook:  
[Databricks Retail Analytics Notebook](./notebook/retail_data_analytics_wrangling_pyspark.ipynb)

---

# Zeppelin and Hadoop Implementation

## Dataset and Analytics Work
The Zeppelin implementation reproduces the same business analytics pipeline using:
- `%spark.pyspark`
- Spark SQL
- Hadoop Distributed File System (HDFS)
- Hive tables
- Zeppelin notebook visualizations using `z.show()`

### Analytics Performed
- JDBC ingestion from PostgreSQL Docker
- CSV ingestion into HDFS
- PySpark ETL
- Revenue analysis
- Country sales analysis
- Product performance analysis
- RFM customer segmentation
- Zeppelin charting

Notebook:  
[Zeppelin Retail Analytics Notebook](./notebook/Spark_Analytics_Notebook.zpln)

---

# Future Improvement
1. **Delta Lake Integration**
   - Upgrade Databricks tables to Delta Lake for ACID transactions and time travel.

2. **Real-Time Streaming**
   - Extend the batch pipeline into real-time analytics using Structured Streaming.

3. **Power BI / Dashboarding**
   - Publish RFM and revenue insights into executive dashboards.

4. **Machine Learning Segmentation**
   - Replace static RFM scoring with clustering models.

5. **CI/CD Data Pipelines**
   - Automate notebook execution and deployment using GitHub Actions and Databricks Jobs.

