Feature: API test

  Background: 
    * url 'http://localhost:8080'
    * header Accept = 'application/json'

  Scenario: Get user with zero followers api
    Given path '/getUser?count=1'
    When method GET
    Then status 200
    And print response
    And assert response.response.length == 1
    And match response.response[0].id != null
