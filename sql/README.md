# Introduction

# SQL Queries

###### Table Setup (DDL)

## Database Schema (PostgreSQL)

```sql
-- Create schema
CREATE SCHEMA IF NOT EXISTS cd;

-- =========================
-- Table: cd.members
-- =========================
CREATE TABLE IF NOT EXISTS cd.members (
    memid INTEGER PRIMARY KEY,
    surname VARCHAR(200) NOT NULL,
    firstname VARCHAR(200) NOT NULL,
    address VARCHAR(300),
    zipcode INTEGER,
    telephone VARCHAR(20),
    recommendedby INTEGER,
    joindate TIMESTAMP NOT NULL,
    CONSTRAINT fk_recommendedby
        FOREIGN KEY (recommendedby)
        REFERENCES cd.members(memid)
);

-- =========================
-- Table: cd.facilities
-- =========================
CREATE TABLE IF NOT EXISTS cd.facilities (
    facid INTEGER PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    membercost NUMERIC NOT NULL,
    guestcost NUMERIC NOT NULL,
    initialoutlay NUMERIC NOT NULL,
    monthlymaintenance NUMERIC NOT NULL
);

-- =========================
-- Table: cd.bookings
-- =========================
CREATE TABLE IF NOT EXISTS cd.bookings (
    bookid INTEGER PRIMARY KEY,
    facid INTEGER NOT NULL,
    memid INTEGER NOT NULL,
    starttime TIMESTAMP NOT NULL,
    slots INTEGER NOT NULL,
    CONSTRAINT fk_bookings_facility
        FOREIGN KEY (facid)
        REFERENCES cd.facilities(facid),
    CONSTRAINT fk_bookings_member
        FOREIGN KEY (memid)
        REFERENCES cd.members(memid)
);
```

###### Question 1: Show all members 

```sql
SELECT *
FROM cd.members
```

###### Question 2: Lorem ipsum...

```sql
SELECT blah blah 
```
