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
---

# SQL Queries

---

# Modifying Data (CRUD)

## Question 1 ? Insert new facility
```sql
INSERT INTO cd.facilities
VALUES (9, 'Spa', 20, 30, 100000, 800);
```

## Question 2 ? Insert using calculated ID
```sql
INSERT INTO cd.facilities
VALUES ((SELECT MAX(facid) FROM cd.facilities)+1, 'Spa', 20, 30, 100000, 800);
```

## Question 3 ? Update facility cost
```sql
UPDATE cd.facilities
SET initialoutlay = 10000
WHERE name = 'Tennis Court 2';
```

## Question 4 ? Update using calculation
```sql
UPDATE cd.facilities
SET
    guestcost = guestcost * 1.1,
    membercost = membercost * 1.1
WHERE name = 'Tennis Court 2';
```

## Question 5 ? Delete all bookings
```sql
TRUNCATE TABLE cd.bookings;
```

## Question 6 ? Delete a specific member
```sql
DELETE FROM cd.members
WHERE memid = 37;
```

---

# Basic Queries

## Question 7 ? Facilities with cheap member cost
```sql
SELECT facid, name, membercost, monthlymaintenance
FROM cd.facilities
WHERE membercost < monthlymaintenance * 1/50
AND membercost > 0;
```

## Question 8 ? Facilities containing ?Tennis?
```sql
SELECT *
FROM cd.facilities
WHERE name LIKE '%Tennis%';
```

## Question 9 ? Facilities by specific IDs
```sql
SELECT *
FROM cd.facilities
WHERE facid IN (1,5);
```

## Question 10 ? Members joined after date
```sql
SELECT memid, surname, firstname, joindate
FROM cd.members
WHERE joindate > '2012-09-01 00:00:00';
```

## Question 11 ? UNION members + facilities
```sql
SELECT surname FROM cd.members
UNION
SELECT name FROM cd.facilities;
```

---

# Joins

## Question 12 ? Booking times for David Farrell
```sql
SELECT b.starttime
FROM cd.bookings b
LEFT JOIN cd.members m ON b.memid = m.memid
WHERE m.firstname = 'David'
AND m.surname = 'Farrell';
```

## Question 13 ? Tennis court bookings on a day
```sql
SELECT b.starttime AS start, f.name
FROM cd.bookings b
LEFT JOIN cd.facilities f ON b.facid = f.facid
WHERE b.starttime >= '2012-09-21'
AND b.starttime < '2012-09-22'
AND f.name LIKE '%Tennis Court%'
ORDER BY b.starttime;
```

## Question 14 ? Members and who recommended them (self join)
```sql
SELECT mems.firstname AS memfname, mems.surname AS memsname,
       recs.firstname AS recfname, recs.surname AS recsname
FROM cd.members mems
LEFT JOIN cd.members recs
ON recs.memid = mems.recommendedby
ORDER BY memsname, memfname;
```

## Question 15 ? Members who recommended someone
```sql
SELECT DISTINCT a.firstname, a.surname
FROM cd.members a
JOIN cd.members b
ON a.memid = b.recommendedby
ORDER BY a.surname, a.firstname;
```

## Question 16 ? Subquery + join for recommender names
```sql
SELECT DISTINCT
mems.firstname || ' ' || mems.surname AS member,
(SELECT recs.firstname || ' ' || recs.surname
 FROM cd.members recs
 WHERE recs.memid = mems.recommendedby) AS recommender
FROM cd.members mems
ORDER BY member;
```

---

# Aggregation

## Question 17 ? Count recommendations
```sql
SELECT recommendedby, COUNT(*)
FROM cd.members
WHERE recommendedby IS NOT NULL
GROUP BY recommendedby
ORDER BY recommendedby;
```

## Question 18 ? Total slots per facility
```sql
SELECT facid, SUM(slots) AS total_slots
FROM cd.bookings
GROUP BY facid
ORDER BY facid;
```

## Question 19 ? Slots per facility (month filter)
```sql
SELECT facid, SUM(slots) AS total_slots
FROM cd.bookings
WHERE starttime >= '2012-09-01'
AND starttime < '2012-10-01'
GROUP BY facid
ORDER BY total_slots;
```

## Question 20 ? Slots per facility per month
```sql
SELECT facid,
       EXTRACT(month FROM starttime) AS month,
       SUM(slots) AS total_slots
FROM cd.bookings
WHERE EXTRACT(year FROM starttime) = 2012
GROUP BY facid, month;
```

## Question 21 ? Count distinct members
```sql
SELECT COUNT(DISTINCT memid)
FROM cd.bookings
WHERE slots > 0;
```

## Question 22 ? First booking per member
```sql
SELECT m.surname, m.firstname, b.memid, MIN(b.starttime)
FROM cd.members m
LEFT JOIN cd.bookings b
ON m.memid = b.memid
WHERE b.starttime > '2012-09-01'
GROUP BY m.surname, m.firstname, b.memid
ORDER BY b.memid;
```

## Question 23 ? Window function count
```sql
SELECT COUNT(*) OVER(), firstname, surname
FROM cd.members
ORDER BY joindate;
```

## Question 24 ? Row number ranking
```sql
SELECT ROW_NUMBER() OVER() AS row_number, firstname, surname
FROM cd.members;
```

## Question 25 ? Facility with highest usage
```sql
SELECT facid, total
FROM (
  SELECT facid, SUM(slots) AS total,
         RANK() OVER (ORDER BY SUM(slots) DESC) AS rank
  FROM cd.bookings
  GROUP BY facid
) ranked
WHERE rank = 1;
```

---

# String Functions

## Question 26 ? Concatenate names
```sql
SELECT CONCAT(surname, ', ', firstname)
FROM cd.members;
```

## Question 27 ? Telephone pattern match
```sql
SELECT memid, telephone
FROM cd.members
WHERE telephone LIKE '%(___)%'
ORDER BY memid;
```

## Question 28 ? Group by first letter
```sql
SELECT SUBSTRING(surname,1,1) AS letter, COUNT(*)
FROM cd.members
GROUP BY letter
ORDER BY letter;
```

