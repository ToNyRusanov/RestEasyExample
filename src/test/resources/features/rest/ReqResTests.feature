Feature: ReqRes tests

  @Rest1
  Scenario: Create user
    Given a user lists all available users
    When user "Pesho Peshov" doesn't exist
    Then the user create a new user "Pesho Peshov"

  @Rest1
  Scenario: Update user
    Given a user lists all available users
    When user "George Bluth" exist
    Then the user can be updated to "George BluthDidNiveEleven"
   @Rest 
   Scenario: Delete user
       Given a user lists all available users
       And user check if the user "Janet Weaver" exist 
       Then the user delete a new user by ID
       
       
       
       
       