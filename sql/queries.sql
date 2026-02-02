-- =========================
-- CRUD
-- =========================

-- Q1
INSERT INTO cd.facilities VALUES (9, 'Spa', 20, 30, 100000, 800);

-- Q2
INSERT INTO cd.facilities
VALUES ((SELECT MAX(facid) FROM cd.facilities)+1, 'Spa', 20, 30, 100000, 800);

-- Q3
UPDATE cd.facilities
SET initialoutlay = 10000
WHERE name = 'Tennis Court 2';

-- Q4
UPDATE cd.facilities
SET guestcost = guestcost * 1.1,
    membercost = membercost * 1.1
WHERE name = 'Tennis Court 2';

-- Q5
TRUNCATE TABLE cd.bookings;

-- Q6
DELETE FROM cd.members WHERE memid = 37;


-- =========================
-- Basics
-- =========================

-- Q7
SELECT facid, name, membercost, monthlymaintenance
FROM cd.facilities
WHERE membercost < monthlymaintenance * 1/50 AND membercost > 0;

-- Q8
SELECT * FROM cd.facilities WHERE name LIKE '%Tennis%';

-- Q9
SELECT * FROM cd.facilities WHERE facid IN (1,5);

-- Q10
SELECT memid, surname, firstname, joindate
FROM cd.members
WHERE joindate > '2012-09-01 00:00:00';

-- Q11
SELECT surname FROM cd.members
UNION
SELECT name FROM cd.facilities;


-- =========================
-- Joins
-- =========================

-- Q12
SELECT b.starttime
FROM cd.bookings b
LEFT JOIN cd.members m ON b.memid = m.memid
WHERE m.firstname='David' AND m.surname='Farrell';

-- Q13
SELECT b.starttime, f.name
FROM cd.bookings b
LEFT JOIN cd.facilities f ON b.facid=f.facid
WHERE b.starttime >= '2012-09-21'
AND b.starttime < '2012-09-22'
AND f.name LIKE '%Tennis Court%'
ORDER BY b.starttime;

-- Q14
SELECT mems.firstname, mems.surname, recs.firstname, recs.surname
FROM cd.members mems
LEFT JOIN cd.members recs ON recs.memid = mems.recommendedby;

-- Q15
SELECT DISTINCT a.firstname, a.surname
FROM cd.members a
JOIN cd.members b ON a.memid=b.recommendedby;

-- Q16
SELECT mems.firstname || ' ' || mems.surname,
(SELECT recs.firstname || ' ' || recs.surname
 FROM cd.members recs
 WHERE recs.memid = mems.recommendedby)
FROM cd.members mems;


-- =========================
-- Aggregation
-- =========================

-- Q17
SELECT recommendedby, COUNT(*)
FROM cd.members
WHERE recommendedby IS NOT NULL
GROUP BY recommendedby;

-- Q18
SELECT facid, SUM(slots)
FROM cd.bookings
GROUP BY facid;

-- Q19
SELECT facid, SUM(slots)
FROM cd.bookings
WHERE starttime >= '2012-09-01'
AND starttime < '2012-10-01'
GROUP BY facid;

-- Q20
SELECT facid, EXTRACT(month FROM starttime), SUM(slots)
FROM cd.bookings
GROUP BY facid, EXTRACT(month FROM starttime);

-- Q21
SELECT COUNT(DISTINCT memid)
FROM cd.bookings;

-- Q22
SELECT m.surname, m.firstname, MIN(b.starttime)
FROM cd.members m
LEFT JOIN cd.bookings b ON m.memid=b.memid
GROUP BY m.surname, m.firstname;

-- Q23
SELECT COUNT(*) OVER(), firstname, surname
FROM cd.members;

-- Q24
SELECT ROW_NUMBER() OVER(), firstname, surname
FROM cd.members;

-- Q25
SELECT facid, SUM(slots)
FROM cd.bookings
GROUP BY facid
ORDER BY SUM(slots) DESC
LIMIT 1;


-- =========================
-- String
-- =========================

-- Q26
SELECT CONCAT(surname, ', ', firstname) FROM cd.members;

-- Q27
SELECT memid, telephone
FROM cd.members
WHERE telephone LIKE '%(___)%';

-- Q28
SELECT SUBSTRING(surname,1,1), COUNT(*)
FROM cd.members
GROUP BY 1;
