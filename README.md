# Compliance - Fraud Detection Service

This is a Spring Boot Application that implements fraud detection for transactions, including checks for high-frequency transactions, unusual merchants, and other compliance-related activities. This will only label transaction as fraud with its type as key and it is up to the issuer to take an action accordingly.

## Features

•	**High Frequency Detection:** Flags users with more than 5 transactions within 60 seconds.

•	**Unusual Merchant Detection:** Flags transactions with merchants that the user has not interacted with before.

•	**Sanction List Check:** Checks if a user is in a sanctions list based on user IDs (OFAC, SDN/NON-SDN list can be used in place of this which has person's name and details).

•	**Odd Hour Transactions:** If between 00:00 to 04:00 AM any transaction happens, we label it as fraud.

•	**High Value Transactions:** Checks if transaction is of high value(here I have used 10,000 as the upper-limit)

## Project Structure

Here’s the structure of the project:

```plaintext
compliance/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── atlasfin/
│   │   │           └── compliance/
│   │   │               ├── ComplianceApplication.java
│   │   │               ├── constant/
│   │   │               │   ├── ComplianceConstant.java
│   │   │               ├── controller/
│   │   │               │   └── ComplianceController.java
│   │   │               │── dto/
│   │   │               │   └── Transaction.java
│   │   │               ├── exception/
│   │   │               │   ├── ComplianceException.java
│   │   │               │   ├── ComplianceExceptionHandler.java
│   │   │               ├── service/
│   │   │               │   └── ComplianceService.java
│   │   │               └── util/
│   │   │                   └── ComplianceUtil.java
│   ├── resources/
│   │   ├── AtlasProject.postman_collection.json
│   │   ├── application.properties
│   │   ├── transaction_list.csv
│   │   └── sanctions_list.csv
├── pom.xml
└── README.md

```

•	**transaction_list.csv:** You can use this to attach to postman or curl request which will be passed as multipart request.

•	**sanctions_list.csv:** contains list of users which have been involved in some terrorist or criminal activities.

## Requirements
•	Java 17 (or higher)
•	Maven 3.x (or Gradle)

Installation

Clone the repository:
git clone https://github.com/yourusername/my-spring-boot-fraud-detection.git

Navigate into the project folder:
cd compliance

Build the project

Build the project using Maven:
mvn clean install

Running the Application

After the build completes, run the application:

mvn spring-boot:run

The application will be running at http://localhost:8080.

## API Documentation

POST /fraud-detection/upload

You can curl or use postman with postman file inside resources of project.

curl --location 'http://localhost:8080/fraud-detection/upload' \
--form 'file=@"/path/to/project/compliance/src/main/resources/transaction_list.csv"'

### Request: 
Request body just contains attached file with key 'file' and value as the file as shown in curl request above.


### Response:
•	Status: 200 OK
•	Response Body example below:

```
{
"UNUSUAL_MERCHANT": [
"9cda6a11-18b8-4060-b6bd-d4cf3dw82659"
],

"HIGH_VALUE": [
"10da6a11-18b8-4060-b6bd-d4cf3dw82659",
"10da6a11-18b8-4060-b6bd-d4cf3dw82659",
"10da6a11-18b8-4060-b6bd-d4cf3dw82669"
],

"SANCTION_LIST": [
"we3b6809-8c4c-4dac-9b43-9be5260a0cbc",
"1cda6a11-18b8-4060-b6bd-d4cf3d582659",
"9cda6a11-18b8-4060-b6bd-d4cf3dw82659",
"10da6a11-18b8-4060-b6bd-d4cf3dw82659",
"we3b6809-8c4c-4dac-9b43-9be5260a0cbc",
"1cda6a11-18b8-4060-b6bd-d4cf3d582659",
"9cda6a11-18b8-4060-b6bd-d4cf3dw82659",
"10da6a11-18b8-4060-b6bd-d4cf3dw82659",
"10da6a11-18b8-4060-b6bd-d4cf3dw82669",
"11da6a11-18b8-4060-b6bd-d4cf3dw82659",
"12da6a11-18b8-4060-b6bd-d4cf3dw82659",
"13da6a11-18b8-4060-b6bd-d4cf3dw82659",
"14da6a11-18b8-4060-b6bd-d4cf3dw82659",
"15da6a11-18b8-4060-b6bd-d4cf3dw82659"
],

"ODD_HOUR": [
"15da6a11-18b8-4060-b6bd-d4cf3dw82659"
],

"HIGH_FREQUENCY": [
"1cda6a11-18b8-4060-b6bd-d4cf3d582659",
"9cda6a11-18b8-4060-b6bd-d4cf3dw82659",
"10da6a11-18b8-4060-b6bd-d4cf3dw82659",
"1cda6a11-18b8-4060-b6bd-d4cf3d582659",
"9cda6a11-18b8-4060-b6bd-d4cf3dw82659",
"10da6a11-18b8-4060-b6bd-d4cf3dw82659",
"10da6a11-18b8-4060-b6bd-d4cf3dw82669",
"11da6a11-18b8-4060-b6bd-d4cf3dw82659",
"12da6a11-18b8-4060-b6bd-d4cf3dw82659",
"13da6a11-18b8-4060-b6bd-d4cf3dw82659",
"14da6a11-18b8-4060-b6bd-d4cf3dw82659"
]
}
```


Contribution

Feel free to fork this project, submit pull requests, or open issues for improvements.