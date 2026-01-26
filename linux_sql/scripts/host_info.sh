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

# capture CPU info output once for reuse
lscpu_out=$(lscpu)

# extract total CPU cores
cpu_number=$(echo "$lscpu_out" | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)

# hardware details
cpu_architecture=$(echo "$lscpu_out" | egrep "^Architecture:" | awk '{print $2}' | xargs)
cpu_model=$(echo "$lscpu_out" | egrep "^Model name:" | cut -d: -f2 | xargs)

# CPU MHz may not be available on all machines (especially VMs)
cpu_mhz=$(echo "$lscpu_out" | egrep "^CPU MHz:" | awk '{print $3}' | xargs)
[ -z "$cpu_mhz" ] && cpu_mhz=$(echo "$lscpu_out" | egrep "^CPU max MHz:" | awk '{print $4}' | xargs)
[ -z "$cpu_mhz" ] && cpu_mhz=0

# L2 cache size (keep it as string, as shown in lscpu output)
l2_cache=$(echo "$lscpu_out" | egrep "^L2 cache:" | awk '{print $3}' | xargs)

# total memory in MB
total_mem=$(free -m | awk '/^Mem:/ {print $2}' | xargs)

# timestamp in UTC format
timestamp=$(date -u +"%Y-%m-%d %H:%M:%S")

# build the SQL insert statement
insert_stmt="INSERT INTO host_info (hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache, total_mem, timestamp) VALUES ('$hostname', $cpu_number, '$cpu_architecture', '$cpu_model', $cpu_mhz, $l2_cache, $total_mem, '$timestamp') ON CONFLICT (hostname) DO NOTHING;"

# pass password to psql
export PGPASSWORD=$psql_password

# execute insert query
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"

exit $?

