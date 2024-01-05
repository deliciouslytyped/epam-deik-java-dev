#TODO needs to be filled out
Feature: Screening management in ticket service application
  #TODO perhaps create a Given rule that creates the prereqs for a screening?
  # I could use a background but that might make other independent tests harder to deal with?
  # it's just easier to handle everything within a single transaction.
  Rule: As an Admin (but not as a User), I can create screenings.

    @txn
    Scenario Outline: Create a new screening
      Given we act as if the date is -<localtime>-
      And the "<genre>" movie "<title>", lasting -<runtime>- minutes
      And "<room>" with -<rows>- rows and -<cols>- columns of seats already exists
      When I attempt to create a screening of "<title>" in "<room>" at -<localstarttime>-
      Then the screening for "<title>" in "<room>" at -<localstarttime>- exists
      #And the screening for "<title>" in "room" is assigned an id # Doesnt make sense?

      Examples:
        | localtime                               | genre  | title | runtime | room  | rows | cols | localstarttime                          |
        | 2023-01-01T15:30:45+02:00[Europe/Paris] | comedy | Hank  | 60      | RoomA | 10   | 15   | 2024-05-20T15:30:45+02:00[Europe/Paris] |

    @txn
    Scenario Outline: Can't create a screening in the past
      Given we act as if the date is -<localtime>-
      And the "<genre>" movie "<title>", lasting -<runtime>- minutes
      And "<room>" with -<rows>- rows and -<cols>- columns of seats already exists
      When I attempt to create a screening of "<title>" in "<room>" at -<localstarttime>-
      Then I should receive an <exception> with the message "<errorMessage>"

      Examples:
        | localtime                               | title | genre  | runtime | room  | rows | cols | localstarttime                          | exception                  | errorMessage                              |
        | 2025-01-01T00:00:00+02:00[Europe/Paris] | Hank  | comedy | 60      | RoomA | 10   | 15   | 2024-05-20T15:30:45+02:00[Europe/Paris] | ApplicationDomainException | You can't create a screening in the past. |

    @txn
    Scenario Outline: Can't create a screening overlapping with another screening in the same room
      Given we act as if the date is -<localtime>-
      And the "<genre>" movie "<title>", lasting -<runtime>- minutes
      And "<room>" with -<rows>- rows and -<cols>- columns of seats already exists
      And the screening for "<title>" in "<room>" at -<localstarttime1>- exists
      When I attempt to create a screening of "<title>" in "<room>" at -<localstarttime2>-
      Then I should receive an <exception> with the message "<errorMessage>"

      #TODO multiple cases
      Examples:
        | localtime        | title | genre  | runtime | room  | rows | cols | localstarttime1                         | localstarttime2                         | exception                  | errorMessage                                                                       |
        | 2023-01-01T15:30:45+02:00[Europe/Paris] | Hank  | comedy | 60      | RoomA | 10   | 15   | 2024-05-20T15:30:45+02:00[Europe/Paris] | 2024-05-20T15:50:45+02:00[Europe/Paris] | ApplicationDomainException | You can't create a screening that overaps with another screening in the same room. |

    @txn #TODO extend to checking the boundaries properly
    Scenario Outline: Can't create a screening in the ten minute break period after a movie in a given room
      Given the "<genre>" movie "<title>", lasting -<runtime>- minutes
      And "<room>" with -<rows>- rows and -<cols>- columns of seats already exists
      And the screening for "<title>" in "<room>" at -<localstarttime1>- exists
      When I attempt to create a screening of "<title>" in "<room>" at -<localstarttime2>-
      Then I should receive an <exception> with the message "<errorMessage>"

      #TODO multiple cases
      Examples:
        | title | genre  | runtime | room  | rows | cols | localstarttime1                         | localstarttime2                         | exception                  | errorMessage                                                                                 |
        | Hank  | comedy | 60      | RoomA | 10   | 15   | 2024-05-20T15:30:45+02:00[Europe/Paris] | 2024-05-20T16:35:45+02:00[Europe/Paris] | ApplicationDomainException | You can't create a screening that overlaps with the 10 minute break after another screening. |