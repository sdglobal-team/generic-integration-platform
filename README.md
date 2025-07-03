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
6. Online file-based sources for inbound files

## Tech Stack

- Java 17
- Spring Boot 3.x
- Apache Camel
- Kafka
- ArangoDB
- Lombok

## Flow Overview

1. Inbound file support from:

   1.1 Local data/inbound folder

   1.2 Online file sharing services (Filebase server)
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

## File-based Online Sources
1. Filebase: S3-compatible decentralized object storage platform.

2. You Can Just SignUp and Create Credentials And then create buckets and upload files via the Filebase web console.

3. Public access can be configured to generate pre-signed URLs for your files.

4. Apache Camel can connect to Filebase buckets using the standard S3 component with your Filebase access key and secret key.

5. Filebase is compatible with existing AWS SDKs and tools, so you can seamlessly switch from Amazon S3 to Filebase for storing and retrieving files.