
Feature: Validate response for IBL test API

  @GetEpisodeDetails
  Scenario: Send a valid Request to get episode details

    Given I send a request to the URL to get episode details
    Then the response will return status code
      | statusCode |
      | 200        |
    Then the response time should be less than 1000 milliseconds


  Scenario: Validate that all data items have a valid ID and correct episode type
    Given I send a request to the URL to get episode details
    Then all items in "schedule.elements" should have a non-empty "id"
    And all items in "schedule.elements" should have "episode.type" equal to "episode"


  Scenario: Validate that episode titles are never null or empty
    Given I send a request to the URL to get episode details
    Then all items in "schedule.elements" should have a non-empty "episode.title"

  Scenario: Validate that only one episode is marked as live
    Given I send a request to the URL to get episode details
    Then only one episode in the list should have "episode.live" as true

  Scenario: Validate transmission_start is before transmission_end
    Given I send a request to the URL to get episode details
    Then each item in "schedule.elements" should have "transmission_start" before "transmission_end"
