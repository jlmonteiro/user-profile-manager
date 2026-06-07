Feature: Authentication Filter

  Scenario: Missing auth header returns 401
    When a request is made to "/api/v1/me" without the auth header
    Then the response status is 401
    And the response body contains "Missing X-Auth-Request-Email header"

  Scenario: Health check is accessible without auth
    When a request is made to "/q/health" without the auth header
    Then the response status is 200

  Scenario: Valid auth header is accepted
    When a request is made to "/api/v1/me" with email "jorge@example.com"
    Then the response status is 200
