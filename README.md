# soen487-appointment-system
A general appointment system that uses the Google Calendar API.

## Overview
1. [Requirements](#1-requirements)  
2. [Installation](#2-installation)  
   2.1 [Maven](#21-maven)  
   2.2. [npm](#22-npm)  
3. [Configuration](#3-configuration)  
   3.1 [Database Configuration](#31-database-configuration)  
   3.2 [Google API Credentials Configuration](#32-google-api-credentials-configuration)  
4. [Starting the Application](#4-starting-the-application)  
5. [External Libraries](#5-external-libraries)  
   5.1 [Backend Libraries](#51-backend-libraries)  
   5.2 [Frontend Libraries](#52-frontend-libraries)

## 1. Requirements
- Java 1.8
- MySQL
- Maven
- npm

## 2. Installation
Install this application by using **_Maven_** and **_npm_** as indicated below.

### 2.1 Maven
Use Maven to build the following **_backend_** modules and to download their dependencies:
> - core
> - implementation
> - service

### 2.2 npm
Use npm to download the required **_frontend_** packages for the following module:
> - webapp

## 3. Configuration
Before being able to start the application there are some configurations that need to be changed.

### 3.1 Database Configuration
This application uses **_MySQL_** for its database, so we first need to set that up.

Here is what needs to be done:
1. Create the database tables using the SQL script found in `implementation/src/main/resources/db_create.sql`
2. Change the fields in `db_config.json` to your MySQL database credentials

### 3.2 Google API Credentials Configuration
Since this application uses **_Google Calendar API_** but hasn't been published, Google restricts the allowed users to those that we specified as test users.

Therefore, those credentials need to be changed in `implementation/src/main/resources/googleCredentials.json`

Also, make sure that the credentials provided has access to both **_Google SSO_** and **_Google Calendar API_** as well as the following **_scopes_**:
> - https://www.googleapis.com/auth/userinfo.email
> - https://www.googleapis.com/auth/calendar

## 4. Starting the Application
Considering that the installation and configuration was done correctly, to run the application just follow these steps:
1. Make sure the **_MySQL_** server is running
2. Start the application's **_backend_** by running `service/src/main/java/com/example/Main.java`
3. Start the application's **_frontend_** by running the command `npm start` in the `webapp` directory

## 5. External Libraries
As indicated in the `pom.xml` or the `package.json` files of each module, here is a list of the external libraries that this application uses.  

### 5.1 Backend Libraries
| Group Id                        | Artifact Id                    | Version          |
| :-----------------------------  | :----------------------------- | :--------------- |
| com.github.scribejava           | scribejava-apis                | 8.0.0            |
| com.github.scribejava           | scribejava-core                | 8.0.0            |
| com.google.apis                 | google-api-services-calendar   | v3-rev411-1.25.0 |
| com.google.apis                 | google-api-services-oauth2     | v2-rev120-1.22.0 |
| com.google.api-client           | google-api-client              | 1.30.10          |
| com.google.http-client          | google-http-client-jackson2    | 1.36.0           | 
| com.google.oauth-client         | google-oauth-client            | 1.31.0           |
| com.googlecode.json-simple      | json-simple                    | 1.1.1            |
| javax.xml.bind                  | jaxb-api                       | 2.3.0            |
| mysql                           | mysql-connector-java           | 8.0.21           |
| org.apache.maven.plugins        | maven-compiler-plugin          | 2.5.1            |
| org.codehaus.mojo               | exec-maven-plugin              | 1.2.1            |
| org.glassfish.jersey            | jersey-bom                     | 2.17             |
| org.glassfish.jersey.media      | jersey-media-moxy              | 2.17             |
| org.glassfish.jersey.containers | jersey-container-grizzly2-http | 2.17             |

### 5.2 Frontend Libraries
| Package Name                | Version |
| :-------------------------- | :------ |
| @material-ui/core           | 4.11.3  |
| @material-ui/icons          | 4.11.2  |
| @testing-library/jest-dom   | 5.11.9  |
| @testing-library/react      | 11.2.5  |
| @testing-library/user-event | 12.8.3  |
| axios                       | 0.21.1  |
| bootstrap                   | 4.6.0   |
| history                     | 4.10.1  |
| react                       | 17.0.2  |
| react-calendar              | 3.3.1   |
| react-dom                   | 17.0.2  |
| react-router                | 5.2.0   |
| react-router-dom            | 5.2.0   |
| react-scripts               | 4.0.3   |
| web-vitals                  | 1.1.1   |


## 6. Service API Documentation

### 6.1 Admin Service API

#### Description
The Admin API handles functionalities related to the authentication of administrative users. Admins can view all appointments from of any resources (person or facility). Admins user are users native to our appointment systems and are stored in the application database.

#### Operations

**1. Log in:** Log in a adminsitrative user
```
POST  http://localhost:8081/admin/login
Accepted content type: application/x-www-form-urlencoded
Return type: application/json
``` 
- Parameters
```
username: the username of the admin user
password: the password of the admin user
```
- Returned value
```
User object with authentication token
```

**2. Log out:** Log out a adminsitrative user (*)
```
POST  http://localhost:8081/admin/logout
Accepted content type: application/x-www-form-urlencoded
Return type: None
``` 
- Parameters
```
None
```
- Returned value
```
None
```

**3. Authenticate:** ? (*)
```
POST  http://localhost:8081/admin/auth
Accepted content type: application/x-www-form-urlencoded
Return type: application/json
``` 
- Parameters
```
None
```
- Returned value
```
None
```

**(*)**: Operations requires the use of authentication headers. See 6.5.

### 6.2 User Service API

#### Description:
The User API handles functionalities related to the authentication of customers who wants to book appointment using our application. The registration and authentication process are handled by Google. 

#### Operations:

**1. Log in:** Log in a customer user using Google SSO.
```
GET  http://localhost:8081/user/login
Accepted content type: None
Return type: None
``` 
- Parameters
```
None
```
- Returned value
```
None
```

**2. Get token:** Acquires access token from Google Services.
```
GET  http://localhost:8081/user/token
Accepted content type: None
Return type: application/json
``` 
- Parameters
```
?
```
- Returned value
```
User object with authentication token from Google Services.
```

**3. Log out:** Log out a adminsitrative user (*)
```
POST  http://localhost:8081/user/logout
Accepted content type: application/x-www-form-urlencoded
Return type: None
``` 
- Parameters
```
None
```
- Returned value
```
None
```

**4. Authenticate:** ? (*)
```
POST  http://localhost:8081/user/auth
Accepted content type: application/x-www-form-urlencoded
Return type: application/json
``` 
- Parameters
```
?
```
- Returned value
```
?
```

**(*)**: Operations requires the use of authentication headers. See 6.5.

### 6.3 Resource Service API

#### Description:
The Resource Service handle operations related to resources (in our case, resources mean a person or facility/equipment you would like to book). 

#### Operations:
**1. Get resources:** ? (*)
```
GET  http://localhost:8081/resource
Accepted content type: None
Return type: application/json
``` 
- Parameters
```
None
```
- Returned value
```
List of resources created by the admin user           
```

**2. Create resource:** Create a new resource. This operation is available to admin users only. (*)
```
POST  http://localhost:8081/resource
Accepted content type: application/x-www-form-urlencoded
Return type: application/json
``` 
- Parameters
```
name: Name of the resource
```
- Returned value
```
The created resource
```