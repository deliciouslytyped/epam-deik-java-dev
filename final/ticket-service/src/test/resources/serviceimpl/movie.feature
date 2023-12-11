Feature: Movie management in ticket service application

  Rule: As an Admin (but not as a User), I can create movies.

#    Background:
#      Given the "action" movie "MovieA", lasting -60- minutes
#      And the "adventure" movie "MovieB", lasting -70- minutes
#      And the "horror" movie "MovieC", lasting -80- minutes

    Scenario Outline: Create a new movie
      #Do I couple this layer with the database or not?
      Given the movie "<title>" does not exist
      When I attempt to create the "<genre>" movie "<title>", lasting -<runtime>- minutes
      Then the "<genre>" movie "<title>" exists, lasting -<runtime>- minutes

      Examples:
        | title | genre  | runtime |
        | Saw   | horror | 60      |

    Scenario Outline: Attempt to create a movie with existing name
      Given the "action" movie "MovieA", lasting -60- minutes
      When I attempt to create the "<genre>" movie "<title>", lasting -<runtime>- minutes
      Then I should receive an "AlreadyExistsException" with the message "<errorMessage>"

      Examples:
        | title  | genre  | runtime | errorMessage                 |
        | MovieA | action | 15      | Movie MovieA already exists. |

#    Scenario Outline: Attempt to create a movie with invalid parameters
#      #TODO specific exception type
#      Given the movie "<title>" does not exist
#      When I attempt to create the "<genre>" movie "<title>", lasting -<runtime>- minutes
#      Then I should receive an "<exception>" with the message "<errorMessage>"
#
#      Examples:
#        | name  | rowCount | colCount | exception                  | errorMessage                                         |
#        | RoomA | -5       | 15       | ApplicationDomainException | "The number of rows in a room should be positive"    |
#        | RoomA | 0        | 15       | ApplicationDomainException | "The number of rows in a room should be positive"    |
#        | RoomA | 10       | -5       | ApplicationDomainException | "The number of columns in a room should be positive" |
#        | RoomA | 10       | 0        | ApplicationDomainException | "The number of columns in a room should be positive" |
#
#  # Update
  Rule: As an Admin (but not as a User), I can update movies.

    Scenario Outline: Update an existing movie
      Given the "<genre>" movie "<title>", lasting -<runtime>- minutes
      When I attempt to update the movie "<title>" to "<newGenre>" with a runtime of -<newRuntime>- minutes
      Then the "<newGenre>" movie "<title>" exists, lasting -<newRuntime>- minutes

      Examples:
        | title  | genre  | runtime | newGenre  | newRuntime |
        | MovieA | action | 60      | adventure | 70         |
#
#  Scenario Outline: Update a non-existing room
#    Given the room "<name>" does not exist
#    When I attempt to update the room "<name>" with row count "<rowCount>" and column count "<colCount>"
#    Then I should receive an "ApplicationDomainException" indicating the room does not exist
#
#    Examples:
#      | name  | rowCount | colCount |
#      | RoomA | 10       | 15       |
#
#  Scenario Outline: Update a room with invalid parameters
#    Given the room "<name>" with row count "10" and column count "15" already exists
#    When I attempt to update the room "<name>" with row count "<rowCount>" and column count "<colCount>"
#    Then I should receive an "<exception>" exception with a message "<errorMessage>"
#
#    Examples:
#      | name  | rowCount | colCount | exception                  | errorMessage                                         |
#      | RoomA | -5       | 15       | ApplicationDomainException | "The number of rows in a room should be positive"    |
#      | RoomA | 0        | 15       | ApplicationDomainException | "The number of rows in a room should be positive"    |
#      | RoomA | 10       | -5       | ApplicationDomainException | "The number of columns in a room should be positive" |
#      | RoomA | 10       | 0        | ApplicationDomainException | "The number of columns in a room should be positive" |
#
#  # Delete
#
#  Scenario Outline: Delete an existing room
#    Given the room "<name>" with row count "<rowCount>" and column count "<colCount>" already exists
#    When I attempt to delete the room "<name>"
#    Then the room "<name>" should be deleted successfully
#
#    Examples:
#      | name  | rowCount | colCount |
#      | RoomA | 10       | 15       |
#
#  Scenario Outline: Attempt to delete a non-existing room
#    Given the room "<name>" does not exist
#    When I attempt to delete the room "<name>"
#    Then I should receive an "<exception>" exception with a message "<errorMessage>"
#
#    Examples:
#      | name  | exception                  | errorMessage                   |
#      | RoomA | ApplicationDomainException | The room RoomA does not exist. |
#
#  # List
#
#  Scenario: List all rooms
#    Given the room "RoomA" with row count "10" and column count "25" already exists
#    And the room "RoomB" with row count "15" and column count "30" already exists
#    And the room "RoomC" with row count "20" and column count "35" already exists
#    When I request a list of all rooms
#    Then I should receive a list containing all the rooms with the names "[RoomA, RoomB, RoomC]"
#
#  Scenario: List rooms when no rooms exist
#    Given no rooms are created in the system
#    When I request a list of all rooms
#    Then I should receive an empty list