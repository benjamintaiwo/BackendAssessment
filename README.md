# Backend Assessment 

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/6ecea5309d034f6b88b0667d33f850d9)](https://app.codacy.com/gh/benjamintaiwo/BackendAssessment?utm_source=github.com&utm_medium=referral&utm_content=benjamintaiwo/BackendAssessment&utm_campaign=Badge_Grade_Settings)

### A simple REST API backend application with spring boot
## Features
*   Create user 
*   Update User
*   Delete user
*   GET users
*   Onbaording and offboarding email set up
## STRUCTURE
   The application is structured around a layered architecture involving the controller, the service layer and the data access layer. I followed a pattern that ensures 
the service layer never accepts a model as input and never ever returns one either. 
Here, the controller layer interacts with the service layer to perform an operation before reaching the data access layer whenever it receives a request from the api layer.  As such, the controller does not have access to the model objects but converse in terms of neutral DTOs.

The service receives a DTO and make sense out of it by querying for the corresponding model object from database through the repository, 
performed operation(s) and returns a response DTO to the calling service.

This approach allows me to change the view and models independently without having to worry one might break the other.

## Tools and Dependencies
*   Java 11
*   Spring Boot 
*   Springboot jpa
*   Maven
*   MySQL
*   Swagger
*   Amazon SDK SES
*   Jacoco
*   Codacy and Github Action
## HOW TO USE
*   fork the repo and run the the project;
*   Do mvn install or you run mvn package
*   mvn spring-boot:run
*   locate http://localhost:8080/api/swagger-ui.html  --You will find the documentaion of the endpoints on the swagger page
*   You can also test with Postman; the application consumes and produce both json and xml
