Feature: Books Test


  Scenario: Get all books
    Given I have access to the Bookstore API
    When I send a "GET" request to "/BookStore/v1/Books"
    Then I should receive a 200 OK response
    And the response should contain a list of books


  Scenario: Get a single books
    Given I have access to the Bookstore API
    When I send a "GET" request to "/BookStore/v1/Books"
    And I send a "GET" request to "/BookStore/v1/Book" with "?ISBN="
    Then I should receive a 200 OK response
    And the response should contain the following
      | isbn          | 9781449325862 |
      | title         | Git Pocket Guide |
      | subTitle      | A Working Introduction |
      | author        | Richard E. Silverman |
      | publish_date  | 2020-06-04T08:48:39.000Z |
      | publisher     | O'Reilly Media |
      | pages         | 234 |
      | description   | This pocket guide is the perfect on-the-job companion to Git, the distributed version control system. It provides a compact, readable introduction to Git for new users, as well as a reference to common commands and procedures for those of you with Git exp |
      | website       | http://chimera.labs.oreilly.com/books/1230000000561/index.html |