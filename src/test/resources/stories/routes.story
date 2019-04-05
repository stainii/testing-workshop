Feature: Get routes, current and average delay

Narrative:
As a user
I want to see the current delay
and the average delay
for each route

Scenario: get all routes and their current + average delay, most common scenario
Given there is 20 seconds delay from Brussels-North to Zottegem
And at 2019-01-01 there was 10 seconds delay from Brussels-North to Zottegem
When I get all routes and their request status
Then I get 1 route back
And the current delay of route 1 is 20 seconds
And the average delay of route 1 is 15 seconds

Scenario: get all routes and their current + average delay, but with multiple checks on the same day
Given there is 20 seconds delay from Brussels-North to Zottegem
And today there was 10 seconds delay from Brussels-North to Zottegem
When I get all routes and their request status
Then I get 1 route back
And the current delay of route 1 is 20 seconds
And the average delay of route 1 is 20 seconds

Scenario: get all routes and their current + average delay, first use of the application
Given there is 20 seconds delay from Brussels-North to Zottegem
And there were no lookups in the past
When I get all routes and their request status
Then I get 1 route back
And the current delay of route 1 is 20 seconds
And the average delay of route 1 is 20 seconds

Scenario: get all routes and their current + average delay, cache
Given there is 20 seconds delay from Brussels-North to Zottegem
And there were no lookups in the past
When I get all routes and their request status
And I get all routes and their request status
Then I get 1 route back
And the current delay of route 1 is 20 seconds
And the average delay of route 1 is 20 seconds
And the second lookup for Brussels-North to Zottegem was pulled out of the cache
