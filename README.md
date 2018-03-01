# My Retail

A RESTful service that can retrieve and update product and price details by ID.

The application performs the following actions:<br>
GET request at /products/{id} which delivers product data asJSON (where {id} will be a number.
```
Example Response:
{
    "id":13860428,
    "name":"The Big Lebowski (Blu-ray) (Widescreen)",
    "currentPrice":{
        "value": 13.49,
        "currency_code":"USD"
        }
 }
```

PUT request at /products/{id} which updates the product's current price data asJSON (where {id} will be a number. There will be no response.
```
Example Request:
{
    "id":13860428,
    "name":"The Big Lebowski (Blu-ray) (Widescreen)",
    "currentPrice":{
        "value": 13.49,
        "currency_code":"USD"
        }
 }
```



## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

Windows<br>
MongoDB<br>
Java 8 or above<br>
Set the JAVA_HOME environment variable to the installed Java location


### Installing

Unpackage the zip file to your desired directory

Open cmd and cd to the root of the project

```
...\my-retail-master
```

Execute the build with the Wrapper without having to install the Gradle runtime. The project will be built and any tests will be executed.

```
gradle.bat build
```



## Running the tests

The tests were executed with the previous command: gradle.bat build

### Test Results
The test results are available in a html file. Open the html file at:

```
...\my-retail-master\build\reports\tests\test\index.html
```

If needed, to build the project and run the tests again, execute the gradle wrapper command

```
gradlew build
```

## Deployment

To change mongodb connection settings or to change the tomcat host location from the default location of: localhost:8080 can be done in application.properties which is found at:
```
\my-retail-master\src\main\resources\application.properties
```

Changing the tomcat host location requires adding property values to application.properties

```
server.address=<your_ip>
server.port=<your_port>
```

The application can be ran from an embedded Tomcat server using the command from the project's root directory

```
gradlew bootrun
```
