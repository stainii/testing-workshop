# Testing demo
This is a demo application for my workshop about testing.
For more information, please contact [stijn.hooft@realdolmen.com](mailto:stijn.hooft@realdolmen.com).

## Starting the application
With Maven, you can build and run the app.
`mvn spring-boot:run`

## Running the tests
You can run the test with Maven.

By default, only unit tests will be run, by using `mvn clean verify`

If you want to run the integration tests, use `mvn clean verify -Pintegration-tests`.

If you want to run the end to end tests, use `mvn clean verify -Pend-to-end-tests`. Note you need to start the application (at `http://localhost:8080`) before running the end to end tests!

## The database
To make this application plug-and-play, I've included an **in-memory database**.

This means that you don't need to configure your own database before running this application: the database is contained inside the app itself.

To access this database, run the app and go to http://localhost:8080/h2-console.

Log in with:
 * driver class: org.h2.Driver
 * jdbc url: jdbc:h2:mem:testing_demo;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE.
 * user name: sa
 * password: [empty]
