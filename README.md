# APITestAutomation
Automation Framewrok and scripts for Rest API service puzzle


Automation project with Rest-Assured , Cucumber and TestNG.

How to run the project ::

Install java version 1.8 and set the classpath and verify java version (If not already installed in the machine).)
Install maven and set the classpath and verify maven installation with mvn --version command. (If not already installed in the machine).
Now,After unzipping go to the root of the folder and open command prompt.
Now, enter either mvn install or mvn test to run the test class of the Puzzle.
While the program is running, we can see that logs will be shown and all the test pass and failure will be shown.
The build will fail because test cases will fail due to difference in the Rest API documentation and REST APIs response .

Project Structure ::

I have created pom.xml for dependency management.
Rest-Assured  for rest api calls, Cucumber and TestNg for clarity in the test case function names and easy reporting respectively.
I have added model class for request and response and also verified he schema of the request and response by using POJOs.
I have created feature file for test related data and Config.properties file configuration related data.
There is one TestRunner class from where the execution is starting.

Test running flow ::
Pom.xml --> TestRunner.java -->E2ETest.java
