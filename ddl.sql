-- Switch to host_agent database
\c host_agent;

-- Create host_info table
CREATE TABLE IF NOT EXISTS host_info (
    id SERIAL PRIMARY KEY,
    hostname VARCHAR(255) NOT NULL UNIQUE,
    cpu_number INTEGER NOT NULL,
    cpu_architecture VARCHAR(50) NOT NULL,
    cpu_model VARCHAR(255) NOT NULL,
    cpu_mhz NUMERIC NOT NULL,
    l2_cache VARCHAR(50),
    total_mem INTEGER NOT NULL,
    timestamp TIMESTAMP NOT NULL
);

-- Create host_usage table
CREATE TABLE IF NOT EXISTS host_usage (
    id SERIAL PRIMARY KEY,
    timestamp TIMESTAMP NOT NULL,
    host_id INTEGER NOT NULL,
    memory_free INTEGER NOT NULL,
    cpu_idle NUMERIC NOT NULL,
    cpu_kernel NUMERIC NOT NULL,
    disk_io INTEGER NOT NULL,
    disk_available INTEGER NOT NULL,
    FOREIGN KEY (host_id) REFERENCES host_info(id)
);

