# File Integration POC

This is a proof of concept for a generic file integration platform using:
- Spring Boot
- Apache Camel
- Kafka
- ArangoDB

## Features

1. Inbound file polling (CSV / JSON / XML)
2. Apache Camel routes for data flow
3. Kafka used for decoupling producer/consumer
4. Spring Data ArangoDB to store records
5. Outbound file generation from ArangoDB

## Tech Stack

- Java 17
- Spring Boot 3.x
- Apache Camel
- Kafka
- ArangoDB
- Lombok

## Flow Overview

1. Drop files into `data/inbound` folder.
   - Supports CSV, JSON, XML
2. Camel picks up the files and transforms them to JSON
3. JSON data is published to Kafka topic `file-data`
4. Camel consumer reads from Kafka and stores records into ArangoDB
5. Every minute, outbound route generates a JSON file from ArangoDB records in `data/outbound`

## Running Locally

1. Start Kafka on localhost:9092
2. Start ArangoDB on localhost:8529
3. Build with `mvn clean install`
4. Run with `mvn spring-boot:run`

## Folder Structure

- `data/inbound`  → Drop inbound files here
- `data/outbound` → Processed outbound files will appear here

## Sample File Formats

Added in file structure folder.