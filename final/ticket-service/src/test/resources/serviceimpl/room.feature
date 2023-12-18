Feature: Room management in ticket service application

  Rule: As an Admin (but not as a User), I can create movies.

    @txn
    Scenario Outline: Create a new room
      #Do I couple this layer with the database or not?
      Given the room "<name>" does not exist
      When I attempt to create a "<name>" with -<rowCount>- rows and -<colCount>- columns of seats
      Then "<name>" exists with -<rowCount>- rows and -<colCount>- columns of seats

      Examples:
        | name  | rowCount | colCount |
        | RoomD | 10       | 15       |

    @txn
    Scenario Outline: Attempt to create a room with existing name
      Given "<name>" with -<rowCount>- rows and -<colCount>- columns of seats already exists
      When I attempt to create a "<name>" with -<rowCount>- rows and -<colCount>- columns of seats
      Then I should receive an AlreadyExistsException with the message "<errorMessage>"

      Examples:
        | name  | rowCount | colCount | errorMessage                   |
        | RoomA | 10       | 15       | The room RoomA already exists. |

    @txn
    Scenario Outline: Attempt to create a room with invalid parameters
      #Doesn't deal with multiple constraint failures at once.
      #TODO specific exception type
      Given the room "<name>" does not exist
      When I attempt to create a "<name>" with -<rowCount>- rows and -<colCount>- columns of seats
      Then I should receive an <exception> with the message "<errorMessage>"

      Examples:
        | name  | rowCount | colCount | exception                  | errorMessage                                               |
        | RoomA | -5       | 15       | ApplicationDomainException | The number of rows in a room must be a positive number.    |
        | RoomA | 0        | 15       | ApplicationDomainException | The number of rows in a room must be a positive number.    |
        | RoomA | 10       | -5       | ApplicationDomainException | The number of columns in a room must be a positive number. |
        | RoomA | 10       | 0        | ApplicationDomainException | The number of columns in a room must be a positive number. |

  Rule: As an Admin (but not as a User), I can update rooms.

    @txn
    Scenario Outline: Update an existing room
      Given "<name>" with -10- rows and -15- columns of seats already exists
      When I attempt to update the room "<name>" to have -<newRowCount>- rows and -<newColCount>- columns of seats
      Then "<name>" exists with -<newRowCount>- rows and -<newColCount>- columns of seats

      Examples:
        | name  | newRowCount | newColCount |
        | RoomA | 20          | 25          |

    @txn
    Scenario Outline: Update a non-existing room
      Given the room "<name>" does not exist
      When I attempt to update the room "<name>" to have -<newRowCount>- rows and -<newColCount>- columns of seats
      Then I should receive an <exception> with the message "<message>"

      Examples:
        | name  | newRowCount | newColCount | exception                  | message                        |
        | RoomA | 10          | 15          | ApplicationDomainException | The room RoomA does not exist. |

    @txn
    Scenario Outline: Update a room with invalid parameters
      Given "<name>" with -10- rows and -15- columns of seats already exists
      When I attempt to update the room "<name>" to have -<newRowCount>- rows and -<newColCount>- columns of seats
      Then I should receive an <exception> with the message "<errorMessage>"

      Examples:
        | name  | newRowCount | newColCount | exception                  | errorMessage                                                 |
        | RoomA | -5          | 15          | ApplicationDomainException | The number of rows in a room must be a positive number.    |
        | RoomA | 0           | 15          | ApplicationDomainException | The number of rows in a room must be a positive number.    |
        | RoomA | 10          | -5          | ApplicationDomainException | The number of columns in a room must be a positive number. |
        | RoomA | 10          | 0           | ApplicationDomainException | The number of columns in a room must be a positive number. |

  Rule: As an Admin (but not as a User), I can delete movies.

    @txn
    Scenario Outline: Delete an existing room
      Given "<name>" with -<rowCount>- rows and -<colCount>- columns of seats already exists
      When I attempt to delete the room "<name>"
      Then the room "<name>" does not exist

      Examples:
        | name  | rowCount | colCount |
        | RoomA | 10       | 15       |

    @txn
    Scenario Outline: Attempt to delete a non-existing room
      Given the room "<name>" does not exist
      When I attempt to delete the room "<name>"
      Then I should receive an <exception> with the message "<errorMessage>"

      Examples:
        | name  | exception                  | errorMessage                   |
        | RoomA | ApplicationDomainException | The room RoomA does not exist. |

  Rule: As any kind of user, I can list rooms.

    @txn
    Scenario: List all rooms
      Given "RoomA" with -10- rows and -25- columns of seats already exists
      And "RoomB" with -15- rows and -30- columns of seats already exists
      And "RoomC" with -20- rows and -35- columns of seats already exists
      When I request a list of all movies
      Then I should receive a list of all the rooms with the names "[RoomA, RoomB, RoomC]"

    @txn
    Scenario: List rooms when no rooms exist
      Given there are no rooms in the system
      When I request a list of all rooms
      Then I should receive an empty room list