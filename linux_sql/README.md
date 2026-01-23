# Linux Cluster Monitoring Agent

## Introduction

This project is a lightweight Linux cluster monitoring agent designed to collect and store both static hardware information and dynamic resource usage metrics from Linux hosts. The goal is to provide a simple and reliable way to track system health over time using standard Linux tools and a centralized PostgreSQL database.

Each Linux node runs a small Bash-based agent that gathers CPU, memory, and disk metrics using native commands such as lscpu, vmstat, and df. Hardware information is collected once per host, while usage metrics are captured periodically and stored for historical analysis. PostgreSQL is used as the backend database, running inside a Docker container for easy setup and portability.

This project is intended for system administrators, DevOps engineers, or anyone interested in monitoring Linux infrastructure using open-source tools like Bash, Docker, Git, and PostgreSQL.

---

## Quick Start

```bash
# Start PostgreSQL using Docker
bash scripts/psql_docker.sh start postgres password

# Create database tables
psql -h localhost -U postgres -d host_agent -f sql/ddl.sql

# Insert host hardware information (run once per host)
bash scripts/host_info.sh localhost 5432 host_agent postgres password

# Insert host usage metrics (can be run manually)
bash scripts/host_usage.sh localhost 5432 host_agent postgres password

# Set up cron job
crontab -e
```

Example cron entry:
```bash
* * * * * bash /absolute/path/to/scripts/host_usage.sh localhost 5432 host_agent postgres password >> /tmp/host_usage.log 2>&1
```

---

## Implementation

### Architecture

![Architecture Diagram](assets/architecture.png)

The system follows a simple agent-based architecture. Each Linux host runs two Bash scripts that act as monitoring agents. These agents collect data locally and send it to a centralized PostgreSQL database. The database runs inside a Docker container, making it easy to deploy and manage.

---

### Scripts

#### psql_docker.sh
Manages the lifecycle of a PostgreSQL Docker container and ensures the database service is available before agents run.

```bash
bash scripts/psql_docker.sh start postgres password
```

---

#### host_info.sh
Collects static hardware details such as CPU count, architecture, CPU model, total memory, and cache size. This script is intended to be executed once per host.

```bash
bash scripts/host_info.sh psql_host psql_port db_name psql_user psql_password
```

---

#### host_usage.sh
Collects runtime system metrics including free memory, CPU usage, disk I/O, and available disk space. This script is designed to be executed repeatedly, typically via crontab.

```bash
bash scripts/host_usage.sh psql_host psql_port db_name psql_user psql_password
```

---

#### crontab
Used to schedule periodic execution of the host_usage.sh script for continuous monitoring.

---

#### queries.sql
Contains SQL queries that help analyze collected data, such as average memory usage, CPU utilization trends, and disk availability across hosts.

---

## Database Modeling

### host_info

| Column Name | Description |
|------------|-------------|
| id | Unique identifier for each host |
| hostname | Fully qualified domain name |
| cpu_number | Number of CPU cores |
| cpu_architecture | CPU architecture |
| cpu_model | CPU model name |
| cpu_mhz | CPU clock speed |
| l2_cache | L2 cache size |
| total_mem | Total memory in MB |
| timestamp | Record creation time (UTC) |

---

### host_usage

| Column Name | Description |
|------------|-------------|
| id | Unique usage record |
| timestamp | Data collection time (UTC) |
| host_id | Reference to host_info |
| memory_free | Free memory in MB |
| cpu_idle | Idle CPU percentage |
| cpu_kernel | Kernel CPU percentage |
| disk_io | Disk I/O metric |
| disk_available | Available disk space in MB |

---

## Test

Scripts were tested manually by running them on Linux hosts and validating inserted records using SQL queries. The DDL script was executed multiple times to ensure tables were created safely without duplication. Cron execution was verified by inspecting logs and database entries.

---

## Deployment

The project is hosted on GitHub for version control. PostgreSQL is deployed using Docker for consistency across environments. Bash monitoring agents are deployed directly on Linux hosts, and cron is used to automate metric collection.

---

## Improvements

- Detect and handle hardware changes dynamically
- Add alerting for resource thresholds
- Improve logging and error handling
- Extend monitoring to network and per-disk metrics
- Integrate dashboards using Grafana or similar tools

