Feature: Validate Manual TestCases for IBL test API

  Scenario: Validate TV licence requirement
Given I send a request to the URL to get episode details
Then each episode should have "requires_tv_licence" as true

Scenario: Validate duration format starts with PT
Given I send a request to the URL to get episode details
Then each episode should have "duration.value" starting with "PT"

Scenario: Validate master brand name is BBC One
Given I send a request to the URL to get episode details
Then each episode should have master brand large title as "BBC One"

Scenario: Validate presence of dax and optimizely event systems
Given I send a request to the URL to get episode details
Then each episode should contain event systems "dax" and "optimizely"