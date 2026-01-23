
#!/bin/bash

# capture CLI arguments into variables
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

# ensure correct number of inputs
if [ "$#" -ne 5 ]; then
  echo "Illegal number of parameters"
  exit 1
fi

# get the machine hostname
hostname=$(hostname -f)

# capture CPU and memory usage output once for reuse
vmstat_out=$(vmstat --unit M | tail -1)

# timestamp in UTC format
timestamp=$(date -u +"%Y-%m-%d %H:%M:%S")

# usage metrics
memory_free=$(echo "$vmstat_out" | awk '{print $4}')
cpu_idle=$(echo "$vmstat_out" | awk '{print $15}')
cpu_kernel=$(echo "$vmstat_out" | awk '{print $14}')

# disk IO and available space
disk_io=$(vmstat -d | tail -1 | awk '{print $10}')
disk_available=$(df -BM / | tail -1 | awk '{print $4}' | tr -d 'M')

# SQL insert statement
insert_stmt="INSERT INTO host_usage (timestamp, host_id, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available) VALUES ('$timestamp', (SELECT id FROM host_info WHERE hostname='$hostname'), $memory_free, $cpu_idle, $cpu_kernel, $disk_io, $disk_available);"

# pass password to psql
export PGPASSWORD=$psql_password

# execute insert query
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"

exit $?

