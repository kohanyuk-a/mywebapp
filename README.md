# MyWebApp - Laboratory Work

## Variant Calculation

N = 15

- V2 = 2
- V3 = 1
- V5 = 1

## Stack

- Java 17
- Spring Boot
- PostgreSQL
- Nginx
- Systemd

## Run Locally

```bash
mvn clean package
java -jar target/mywebapp-1.0.0.jar
```

## API

### Get all notes

GET /notes

### Create note

POST /notes

Parameters:
- title
- content

### Get note

GET /notes/{id}

## Deployment

```bash
chmod +x scripts/deploy.sh
sudo ./scripts/deploy.sh
```
