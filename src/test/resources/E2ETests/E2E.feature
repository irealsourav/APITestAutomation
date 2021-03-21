Feature: End to end tests for create and assign cars to an user and delete both car and user
Description: To verify E2E flow for customers.


Scenario Outline: create an user and assign a car to that user and update and delete them
Given I hit the baseURL and port of the API
When I create and verify an user with "<name>" and an "<email>"
When I Get and verify the Created user details 
When I create car of "<manufacturer>" and a "<model>" and "<imageUrl>" with and verify it
When I Get and verify the car details
When I update the car of "<updatedmanufactuer>" with "<updatedmodel>" and "<updateimageUrl>" verify the car details 
When I get and verify the car details again
Then I delete both car and user and verify
 
 Examples:
     | name | email | manufacturer | model | imageUrl | updatedmanufactuer | updatedmodel | updateimageUrl |
     | sourav ghosh | irealsourav@gmail.com | Volkswagen | Scirocco | file:///Users/souravg/ | Renault | Talisman | file:///Users/sghosh/ | 

     