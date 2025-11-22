Feature: Accounts Test


  Scenario: Create new account
    Given I have access to the Bookstore API
    When I send a "POST" request to "/Account/v1/User" with body
      | userName | testUser  |
      | password | Test@1234 |
    Then I should receive a 201 OK response