# microservice-github-api-client

#### REST service which returns details of given Github repository

#### Exercices for testing: JUnit, Mockito, WireMock

#

API of the service work:

GET /repositories/{owner}/{repository-name}

and return:
fullName, description, cloneUrl, stars, createdAt -in JSON format.

#         

To build app type: mvn clean install

To build app without running tests: mvn clean install -DskipTests

To start app: java -jar target/githubApp-0.0.1-SNAPSHOT.jar