Feature: Accounts Test


  Scenario: Create new account
    Given I have access to the Bookstore API
    When I create a new user with credentials
      | userName | testUser  |
      | password | Test@1234 |
    Then I should receive a 201 OK response