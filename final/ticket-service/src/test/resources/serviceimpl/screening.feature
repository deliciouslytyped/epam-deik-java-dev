Feature: Screening management in ticket service application
  #TODO perhaps create a Given rule that creates the prereqs for a screening?
  # I could use a background but that might make other independent tests harder to deal with?
  # it's just easier to handle everything within a single transaction.
  Rule: As an Admin (but not as a User), I can create screenings.

    @txn
    Scenario Outline: Create a new screening
      Given the "<genre>" movie "<title>", lasting -<runtime>- minutes
      And "<room>" with -<rows>- rows and -<cols>- columns of seats already exists
      When I attempt to create a screening of "<title>" in "<room>" at -<localstarttime>-
      Then the screening for "<title>" in "<room>" at -<localstarttime>- exists
      #And the screening for "<title>" in "room" is assigned an id # Doesnt make sense?

      Examples:
        | genre  | title | runtime | room  | rows | cols | localstarttime                          |
        | comedy | Hank  | 60      | RoomA | 10   | 15   | 2024-05-20T15:30:45+02:00[Europe/Paris] |

    @txn
    Scenario Outline: Can't create a screening in the past
      Given we act as if the date is "<localtime>"
      And the "<genre>" movie "<title>", lasting -<runtime>- minutes
      And "<room>" with -<rows>- rows and -<cols>- columns of seats already exists
      When I attempt to create a screening of "<title>" in "<room>" at -<localstarttime>-
      Then I should receive an <exception> with the message "<errorMessage>"

      Examples:
        | localtime        | title | genre  | runtime | room  | rows | cols | localstarttime                          | exception                  | errorMessage                               |
        | Jan 1 2025 GMT+1 | Hank  | comedy | 60      | RoomA | 10   | 15   | 2024-05-20T15:30:45+02:00[Europe/Paris] | ApplicationDomainException | "You can't create a screening in the past. |

    @txn
    Scenario Outline: Can't create a screening overlapping with another screening in the same room
      Given the "<genre>" movie "<title>", lasting -<runtime>- minutes
      And "<room>" with -<rows>- rows and -<cols>- columns of seats already exists
      And there is a screening of "<title>" in "<room>" at -<localstarttime1>-
      When I attempt to create a screening of "<title>" in "<room>" at -<localstarttime2>-
      Then I should receive an <exception> with the message "<errorMessage>"

      #TODO multiple cases
      Examples:
        | localtime        | title | genre  | runtime | room  | rows | cols | localstarttime1                         | localstarttime2 | exception                  | errorMessage                                                                                 |
        | Jan 1 2025 GMT+1 | Hank  | comedy | 60      | RoomA | 10   | 15   | 2024-05-20T15:30:45+02:00[Europe/Paris] | placeholder     | ApplicationDomainException | "You can't create a screening that colides with the 10 minute break after another screening. |

    @txn
    Scenario Outline: Can't create a screening in the ten minute break period after a movie in a given room
      Given the "<genre>" movie "<title>", lasting -<runtime>- minutes
      And "<room>" with -<rows>- rows and -<cols>- columns of seats already exists
      And there is a screening of "<title>" in "<room>" at -<localstarttime1>-
      When I attempt to create a screening of "<title>" in "<room>" at -<localstarttime2>-
      Then I should receive an {exceptionName} with the message {string}

      #TODO multiple cases
      Examples:
        | localtime        | title | genre  | runtime | room  | rows | cols | localstarttime   |
        | Jan 1 2025 GMT+1 | Hank  | comedy | 60      | RoomA | 10   | 15   | Jan 1 2024 GMT+1 |