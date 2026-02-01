# Introduction

This project implements a PostgreSQL-based data management solution for a recreational club booking system. The objective is to design a relational database that stores member information, facility details, and booking activity, and then answer business questions using SQL queries.  

The system supports administrators who need to analyze usage trends, manage facilities, and maintain member records. The database enables tasks such as tracking bookings, calculating facility utilization, identifying member behavior, and generating operational insights.  

The project demonstrates practical database engineering skills including schema design (DDL), data manipulation (CRUD), joins, aggregations, window functions, and reporting queries.  

Technologies used include **PostgreSQL, SQL, Docker, Bash, Git, and Linux CLI tools**, ensuring the solution is portable, reproducible, and easy to deploy in a containerized environment.

---

# SQL Queries

---

###### Table Setup (DDL)

```sql
CREATE SCHEMA IF NOT EXISTS cd;

CREATE TABLE cd.members (
    memid INTEGER PRIMARY KEY,
    surname VARCHAR(200) NOT NULL,
    firstname VARCHAR(200) NOT NULL,
    address VARCHAR(300),
    zipcode INTEGER,
    telephone VARCHAR(20),
    recommendedby INTEGER,
    joindate TIMESTAMP NOT NULL,
    FOREIGN KEY (recommendedby) REFERENCES cd.members(memid)
);

CREATE TABLE cd.facilities (
    facid INTEGER PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    membercost NUMERIC NOT NULL,
    guestcost NUMERIC NOT NULL,
    initialoutlay NUMERIC NOT NULL,
    monthlymaintenance NUMERIC NOT NULL
);

CREATE TABLE cd.bookings (
    bookid INTEGER PRIMARY KEY,
    facid INTEGER NOT NULL,
    memid INTEGER NOT NULL,
    starttime TIMESTAMP NOT NULL,
    slots INTEGER NOT NULL,
    FOREIGN KEY (facid) REFERENCES cd.facilities(facid),
    FOREIGN KEY (memid) REFERENCES cd.members(memid)
);
```

---

# Modifying Data (CRUD)

###### Question 1: Insert new facility
```sql
INSERT INTO cd.facilities
VALUES (9, 'Spa', 20, 30, 100000, 800);
```

###### Question 2: Insert using calculated ID
```sql
INSERT INTO cd.facilities
VALUES ((SELECT MAX(facid)+1 FROM cd.facilities), 'Spa', 20, 30, 100000, 800);
```

###### Question 3: Update facility initial cost
```sql
UPDATE cd.facilities
SET initialoutlay = 10000
WHERE name = 'Tennis Court 2';
```

###### Question 4: Increase costs by 10%
```sql
UPDATE cd.facilities
SET guestcost = guestcost * 1.1,
    membercost = membercost * 1.1
WHERE name = 'Tennis Court 2';
```

###### Question 5: Delete all bookings
```sql
TRUNCATE cd.bookings;
```

###### Question 6: Delete a specific member
```sql
DELETE FROM cd.members
WHERE memid = 37;
```

---

# Basic Queries

###### Question 7: Facilities with cheap member cost
```sql
SELECT facid, name, membercost, monthlymaintenance
FROM cd.facilities
WHERE membercost < monthlymaintenance * 1/50
AND membercost > 0;
```

###### Question 8: Facilities containing 'Tennis'
```sql
SELECT *
FROM cd.facilities
WHERE name LIKE '%Tennis%';
```

###### Question 9: Facilities by specific IDs
```sql
SELECT *
FROM cd.facilities
WHERE facid IN (1,5);
```

###### Question 10: Members joined after a date
```sql
SELECT memid, surname, firstname, joindate
FROM cd.members
WHERE joindate > '2012-09-01';
```

###### Question 11: Combine members and facilities
```sql
SELECT surname FROM cd.members
UNION
SELECT name FROM cd.facilities;
```

---

# Joins

###### Question 12: Booking times for David Farrell
```sql
SELECT b.starttime
FROM cd.bookings b
JOIN cd.members m ON b.memid = m.memid
WHERE m.firstname = 'David'
AND m.surname = 'Farrell';
```

###### Question 13: Tennis court bookings on a day
```sql
SELECT b.starttime, f.name
FROM cd.bookings b
JOIN cd.facilities f ON b.facid = f.facid
WHERE b.starttime >= '2012-09-21'
AND b.starttime < '2012-09-22'
AND f.name LIKE '%Tennis Court%'
ORDER BY b.starttime;
```

###### Question 14: Members and their recommenders
```sql
SELECT mems.firstname, mems.surname,
       recs.firstname, recs.surname
FROM cd.members mems
LEFT JOIN cd.members recs
ON recs.memid = mems.recommendedby;
```

###### Question 15: Members who recommended someone
```sql
SELECT DISTINCT a.firstname, a.surname
FROM cd.members a
JOIN cd.members b
ON a.memid = b.recommendedby;
```

###### Question 16: Recommender names via subquery
```sql
SELECT mems.firstname || ' ' || mems.surname AS member,
       (SELECT recs.firstname || ' ' || recs.surname
        FROM cd.members recs
        WHERE recs.memid = mems.recommendedby) AS recommender
FROM cd.members mems;
```

---

# Aggregation

###### Question 17: Count recommendations
```sql
SELECT recommendedby, COUNT(*)
FROM cd.members
WHERE recommendedby IS NOT NULL
GROUP BY recommendedby;
```

###### Question 18: Total slots per facility
```sql
SELECT facid, SUM(slots)
FROM cd.bookings
GROUP BY facid;
```

###### Question 19: Slots per facility (September)
```sql
SELECT facid, SUM(slots)
FROM cd.bookings
WHERE starttime >= '2012-09-01'
AND starttime < '2012-10-01'
GROUP BY facid;
```

###### Question 20: Slots per facility per month
```sql
SELECT facid,
       EXTRACT(month FROM starttime) AS month,
       SUM(slots)
FROM cd.bookings
GROUP BY facid, month;
```

###### Question 21: Count distinct members
```sql
SELECT COUNT(DISTINCT memid)
FROM cd.bookings;
```

###### Question 22: First booking per member
```sql
SELECT memid, MIN(starttime)
FROM cd.bookings
GROUP BY memid;
```

---

# Advanced Queries

###### Question 23: Total members using window function
```sql
SELECT COUNT(*) OVER(), firstname, surname
FROM cd.members;
```

###### Question 24: Row numbering
```sql
SELECT ROW_NUMBER() OVER(), firstname, surname
FROM cd.members;
```

###### Question 25: Facility with highest usage
```sql
SELECT facid
FROM (
    SELECT facid,
           RANK() OVER (ORDER BY SUM(slots) DESC) r
    FROM cd.bookings
    GROUP BY facid
) x
WHERE r = 1;
```

---

# String Functions

###### Question 26: Concatenate names
```sql
SELECT CONCAT(surname, ', ', firstname)
FROM cd.members;
```

###### Question 27: Telephone pattern match
```sql
SELECT memid, telephone
FROM cd.members
WHERE telephone LIKE '%(___)%';
```

###### Question 28: Group by first letter
```sql
SELECT SUBSTRING(surname,1,1) AS letter, COUNT(*)
FROM cd.members
GROUP BY letter
ORDER BY letter;
```

---

