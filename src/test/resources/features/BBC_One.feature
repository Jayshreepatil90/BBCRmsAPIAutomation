
Feature: Validate response for IBL test API

  @GetEpisodeDetails
  Scenario: Send a valid Request to get episode details

    Given I send a request to the URL to get episode details
      | endpoint             |
      | /api/RMSTest/ibltest |
    Then the response will return status code
      | statusCode |
      | 200        |
    Then the response time should be less than 1000 milliseconds

  Scenario: Validate that all data items have a valid ID and correct episode type

    Given I send a request to the URL to get episode details
      | endpoint             |
      | /api/RMSTest/ibltest |
    Then all items in "schedule.elements" should have a non-empty "id"
    And all items in "schedule.elements" should have "episode.type" equal to "episode"


  Scenario: Validate that episode titles are never null or empty

    Given I send a request to the URL to get episode details
      | endpoint             |
      | /api/RMSTest/ibltest |
    Then all items in "schedule.elements" should have a non-empty "episode.title"

  Scenario: Validate that only one episode is marked as live

    Given I send a request to the URL to get episode details
      | endpoint             |
      | /api/RMSTest/ibltest |
    Then only one episode in the list should have "episode.live" as true

  Scenario: Validate transmission_start is before transmission_end

    Given I send a request to the URL to get episode details
      | endpoint             |
      | /api/RMSTest/ibltest |
    Then each item in "schedule.elements" should have "transmission_start" before "transmission_end"

  Scenario: Validate Date header is present in response

    Given I send a request to the URL to get episode details
      | endpoint             |
      | /api/RMSTest/ibltest |
    Then the response header "Date" should be present and not empty

  Scenario: Validate 404 error and error object structure

    Given I send a request to the URL to get episode details
      | endpoint                        |
      | /api/RMSTest/ibltest/2023-09-11 |
    Then the response will return status code
      | statusCode |
      | 404        |
    And the response should contain fields "details" and "http_response_code" in "error"

