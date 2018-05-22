Feature: restEasy example

Scenario: Delete user 
 Given the user lists all available users
 And user check if the user "Janet Weaver" exist
 Then the user delete a selected user by ID
 