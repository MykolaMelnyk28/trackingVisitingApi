# Tracking Visiting API

### It&apos;s a system for tracking patient visits to doctors

### Technical stack
- Java 17
- Spring Boot
- MySQL 8

---
## Starting

The `init-db` directory contains `dump.sql` file.

### Start with Docker
#### Database
Create a `mysql.env` file in the root directory with the following content:
```dotenv
MYSQL_ROOT_PASSWORD=<password>
```

To run a database in a Docker container, execute this command.
```commandline
docker-compose up -d mysql
```

**The database dump will automatically load*

#### Application

Create an `app.env` file in the root directory with the following content:
```dotenv
DB_USERNAME=root
DB_PASSWORD=<password>
DB_URL=jdbc:mysql://mysql:3306/tracking_visiting_db
```

To run a application, execute this command
```commandline
docker-compose up -d tracking-visiting-api
```

### Start without Docker
#### Database
```
mysql -u <username> -p < ./init-db/dump.sql
```
#### Application
```commandline
mvn clean package
```
Create an `env.properties` file in the root directory with the following content:
```properties
spring.datasource.username=<username>
spring.datasource.password=<password>
spring.datasource.url=jdbc:mysql://<db_host>:<db_port>/tracking_visiting_db
```
To run a application, execute this command
```
java -jar ./target/trackingVisitingApi-1.0.0-SNAPSHOT.jar --spring.config.location=classpath:/application.yml,file:env.properties
```

---

### Documentation
Swagger Documentation: [http://localhost:8080/api/swagger-ui/index.html](http://localhost:8080/api/swagger-ui/index.html)

---

## Endpoints

### Base url: http://localhost:8080/api

### GET `/v1/patients`
#### Response
```json
{
  "data": [
    {
      "id": 1,
      "firstName": "John",
      "lastName": "Smith",
      "lastVisits": [
        {
          "id": 1,
          "start": "2025-01-10T10:00",
          "end": "2025-01-10T10:30",
          "doctor": {
            "id": 1,
            "firstName": "Dr. Adam",
            "lastName": "Jonson",
            "totalPatients": 1
          }
        }
      ]
    }
  ],
  "count": 10
}
```

### POST `/v1/visits`
#### Request
```json
{
  "start": "2025-01-10T10:05",
  "end": "2025-01-10T10:30",
  "patientId": 1,
  "doctorId": 1
}
```
#### Response
```json
{
  "id": 2,
  "start": "2025-01-10T10:05",
  "end": "2025-01-10T10:30",
  "doctor": {
    "id": 1,
    "firstName": "Dr. Adam",
    "lastName": "Jonson",
    "totalPatients": 2
  }
}
```

*For more information about other endpoints, see the Swagger documentation.*