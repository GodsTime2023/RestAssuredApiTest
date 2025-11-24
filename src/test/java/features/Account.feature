Feature: Accounts Test


  Scenario: Create new account
    Given I have access to the Bookstore API
    When I create a new user with credentials
      | userName | testUser  |
      | password | Test@1234 |
    Then I should receive a 201 OK response

    Scenario: Get account user by UUID
    Given I have access to the Bookstore API
    When I create a new user with credentials
        | userName | testUserGet |
        | password | Test@1234   |
    Then I should receive a 201 OK response
    And I generate user token
    When I send a "GET" request to account user endpoint "{0}"
    Then I should receive a 200 OK response

  Scenario: Get account user by existing UUID
    Given I have access to the Bookstore API
    And I generate user token with existing username and password
    When I send a "GET" request to account user endpoint "2695e36d-1357-440d-9735-a8c1cfeed563"
    Then I should receive a 200 OK response