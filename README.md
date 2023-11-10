# Java Spring Boot REST-API example
This is a sample Java 17 / Maven / Spring Boot REST-API application.

## How to Run

- Clone this repository
- Make sure you have atleast JDK 17.
- Then run it locally in your preferred IDE that supports Java, or build the project with maven and run the jar-file. 

This application is then reachable on `http://localhost:8080`.

## About the Application
This is just a simple Employee CRUD (without the update part) REST application. It uses an in-memory H2 database to store the Employee entities.

The application is using Controller / Service / Repository layers, where the logic resides in the Service. Could be worth to discuss if throwing exceptions in the Service is the preferred way over returning an `Optional` and handling that in the Controller instead of intercepting them in the `GlobalControllerExceptionHandler`.

The only real logic in the service-layer that exists is that you can't delete an Employee that doesn't exist.

We handle all other validation with annotations, such as max length on names, not blank names, valid and unique email, and then intercepting the errors to wrap them in a more readable error.

There are some tests for both the Service and Controller to verify that it functions as intended.

## API-Documentation

### Get all Employees
```
GET /employees
Accept: application/json
```
#### Response example:
```
HTTP 200 OK
[
    {
        "id": 1,
        "firstName": "Kalle",
        "lastName": "Anka",
        "email": "kalle@anka.se"
    }
]
```

### Create Employee
```
POST /employees
Accept: application/json
Conent-Type: application/json

{
    "firstName": "Kalle",
    "lastName": "Anka",
    "email": "kalle@anka.se"
}
```

#### Response examples:
```
HTTP 201 Created
{
    "id": 1,
    "firstName": "Kalle",
    "lastName": "Anka",
    "email": "kalle@anka.se"
}
```
```
HTTP 400 Bad Request
{
    "title": "Bad Request",
    "statusCode": 400,
    "detailedMessage": "Field firstName is required and must be between 1 and 30 characters long"
}
```
```
HTTP 409 Conflict
{
    "title": "Conflict",
    "statusCode": 409,
    "detailedMessage": "Employee with that email already exists"
}
```

### Delete Employee
#### By Id
```
DELETE /employees/delete/id/1
Accept: application/json
```
#### Response examples:
```
HTTP 200 OK
Successfully deleted Employee with Id 1
```
```
HTTP 404 Not Found
{
    "title": "Not Found",
    "statusCode": 404,
    "detailedMessage": "No Employee with Id 1 found"
}
```
#### By Email
```
DELETE /employees/delete/email/kalle@anka.se
Accept: application/json
```
#### Response examples:
```
HTTP 200 OK
Successfully deleted Employee with Id 1 and email kalle@anka.se
```
```
HTTP 404 Not Found
{
    "title": "Not Found",
    "statusCode": 404,
    "detailedMessage": "No Employee with email kalle@anka.se found"
}
```