# ? Java Grep Application

## ? Introduction

This project implements a lightweight, memory-efficient file search application similar to Linux **grep**, built using **Core Java** and modern **Java 8+ Stream APIs**.  
The app recursively scans directories, reads files lazily, filters lines using regex patterns, and writes matched results to an output file.

The design focuses on **functional programming**, **low memory usage**, and **scalability**.  
Technologies used include Java Streams & Lambdas, SLF4J logging, Maven for build automation, and Docker for containerized deployment.

---

# ? Quick Start

## 1?? Build the project

```bash
mvn clean package
```

This generates:

```
target/grep-1.0-SNAPSHOT.jar
```

---

## 2?? Run locally

```bash
java -jar target/grep-1.0-SNAPSHOT.jar "<regex>" "<rootDir>" "<outputFile>"
```

### Example

```bash
java -jar target/grep-1.0-SNAPSHOT.jar ".*Romeo.*Juliet.*" ./data ./out/grep.txt
```

---

## 3?? Run using Docker

```bash
docker run --rm \
-v $(pwd)/data:/data \
-v $(pwd)/out:/out \
<docker_user>/grep ".*Romeo.*Juliet.*" /data /out/grep.txt
```

---

# ? Implementation

## ? Design Overview

The application follows a **stream-based processing pipeline**:

```
listFiles ? readLines ? filter(regex) ? writeToFile
```

### Key Design Decisions

### ? Lazy loading with Streams
- Files processed one-by-one
- Lines processed one-by-one
- No large Lists stored in memory

### ? Functional style
- Java Streams
- Lambdas
- Method references

### ? Logging
- Uses SLF4J instead of System.out for production-ready logs

---

## ? Pseudocode (process method)

```
process():
    files = listFiles(rootPath)

    open writer

    for each file in files:
        for each line in readLines(file):
            if containsPattern(line):
                write line immediately

    close writer
```

---

## ? Performance Issue

Loading all file contents into memory using `List<String>` can cause **OutOfMemoryError** when processing large datasets.

To fix this, the application uses:
- `BufferedReader`
- `Files.lines()`
- Java Streams

This ensures **lazy evaluation**, meaning only a small portion of data stays in memory at any time.  
As a result, the app can process files much larger than available heap memory (10?20MB).

---

# ? Test

## Manual Testing Steps

### 1?? Prepare sample data

```bash
mkdir data
echo "Romeo loves Juliet" > data/test.txt
echo "Hello world" >> data/test.txt
```

### 2?? Run

```bash
java -jar target/grep-1.0-SNAPSHOT.jar "Romeo" ./data ./out/result.txt
```

### 3?? Verify

```bash
cat out/result.txt
```

Expected output:

```
Romeo loves Juliet
```

---

# ? Deployment (Docker)

The app is containerized for easy distribution and consistent execution across environments.

## Dockerfile

```dockerfile
FROM eclipse-temurin:17-jre-alpine
COPY target/grep*.jar /app/grep.jar
ENTRYPOINT ["java","-jar","/app/grep.jar"]
```

---

## Build Docker image

```bash
docker build -t <docker_user>/grep .
```

---

## Push to Docker Hub

```bash
docker push <docker_user>/grep
```

Now anyone can run the app without installing Java or Maven.

---

# ? Tech Stack

- Java 8+
- Stream & Lambda APIs
- Regex (Pattern)
- SLF4J Logging
- Maven
- Docker
- IntelliJ IDEA

---

# ? Improvements

1. Add parallel file processing for faster performance
2. Add CLI flags (ignore case, recursive, count only, etc.)
3. Support additional output formats (JSON / CSV / Console summary)

---

# ? Key Learning Outcomes

- Functional programming with Streams
- Memory-efficient large file processing
- Regex pattern matching
- Logging best practices
- Maven packaging
- Docker containerization