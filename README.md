# InternalLibrary

## 1. Backend

### Requirements

* JDK 11
* Maven 3.6.3
* PostgreSQL 13.2

### Setup guide

* Create a database named `library` in PostgreSQL

* Make sure that the user credentials, and the connection port provided in the file `src/main/resources/application.yml` match
  the ones you use in your database.
```yaml
spring:
  datasource:
    driver-class-name: org.postgresql.Driver
    url: jdbc:postgresql://localhost:5432/library
    username: postgres
    password: postgres
```

* Add `test` to `contexts` property if you want to fill the created tables with test data.
```yaml
  liquibase:
    enabled: true
    contexts: test
    change-log: classpath:db/changelog/db.changelog-master.xml
```
* You can change the port that the application runs on by changing the `port` property.
```yaml
server:
  port: 7777
```
* Open a terminal in the location with the pom.xml file and download dependencies. 
  You can also run the `install-backend.bat` script instead.
```
mvn clean install
```
* Start the application. You can also run the `run-backend.bat` script instead.
```
mvn spring-boot:run
```

## 2. Frontend

### Requirements

* npm 6.14.11

### Setup guide

* Provide the connection port that your backend uses in the property `apiPort` in file `application.json`.
```json
{
  "apiPort": 7777
}
```
* Open a terminal in `library-ui` and install required dependencies. 
  You can also run the `install-frontend.bat` script instead.
```
npm install
```
* Start the application.
  You can also run the `run-frontend.bat` script instead.
```
npm start
```
* If you want to open the integration tests either run the `open-tests.bat` script or execute a following command.
```
./node_modules/.bin/cypress open
```