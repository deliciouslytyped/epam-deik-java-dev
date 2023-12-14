Feature: Movie management in ticket service application

  Rule: As an Admin (but not as a User), I can create movies.

#    Background:
#      Given the "action" movie "MovieA", lasting -60- minutes
#      And the "adventure" movie "MovieB", lasting -70- minutes
#      And the "horror" movie "MovieC", lasting -80- minutes

    @txn
    Scenario Outline: Create a new movie
      #Do I couple this layer with the database or not?
      Given the movie "<title>" does not exist
      When I attempt to create the "<genre>" movie "<title>", lasting -<runtime>- minutes
      Then the "<genre>" movie "<title>" exists, lasting -<runtime>- minutes

      Examples:
        | title | genre  | runtime |
        | Saw   | horror | 60      |

    @txn
    Scenario Outline: Attempt to create a movie with existing name
      Given the "action" movie "MovieA", lasting -60- minutes
      When I attempt to create the "<genre>" movie "<title>", lasting -<runtime>- minutes
      Then I should receive an AlreadyExistsException with the message "<errorMessage>"

      Examples:
        | title  | genre  | runtime | errorMessage          |
        | MovieA | action | 15      | Movie already exists. |

    @txn
    Scenario Outline: Attempt to create a movie with invalid parameters
      #TODO specific exception type
      Given the movie "<title>" does not exist
      When I attempt to create the "<genre>" movie "<title>", lasting -<runtime>- minutes
      Then I should receive an <exception> with the message "<errorMessage>"

      Examples:
        | title     | genre  | runtime | exception                  | errorMessage                            |
        | Kung Fury | action | 0       | ApplicationDomainException | The run time of the movie should be positive. |
        | Hank      | action | -1      | ApplicationDomainException | The run time of the movie should be positive. |

  Rule: As an Admin (but not as a User), I can update movies.

    @txn
    Scenario Outline: Update an existing movie
      Given the "<genre>" movie "<title>", lasting -<runtime>- minutes
      When I attempt to update the movie "<title>" to "<newGenre>" with a runtime of -<newRuntime>- minutes
      Then the "<newGenre>" movie "<title>" exists, lasting -<newRuntime>- minutes

      Examples:
        | title  | genre  | runtime | newGenre  | newRuntime |
        | MovieA | action | 60      | adventure | 70         |

    @txn
    Scenario Outline: Update a non-existing movie
      Given the movie "<title>" does not exist
      When I attempt to update the movie "<title>" to "<newGenre>" with a runtime of -<newRuntime>- minutes
      Then I should receive an <exception> with the message "<errorMessage>"

      Examples:
        | title     | newGenre | newRuntime | exception                  | errorMessage                        |
        | Kung Fury | comedy   | 15         | ApplicationDomainException | The movie Kung Fury does not exist. |

    @txn
    Scenario Outline: Update a movie with invalid parameters
      Given the "<genre>" movie "<title>", lasting -<runtime>- minutes
      When I attempt to update the movie "<title>" to "<newGenre>" with a runtime of -<newRuntime>- minutes
      Then I should receive an <exception> with the message "<errorMessage>"

      Examples:
        | title     | genre  | runtime | newGenre  | newRuntime | exception                  | errorMessage                            |
        | Kung Fury | action | 60      | adventure | 0          | ApplicationDomainException | The movie run time needs to be a positive number. |
        | Hank      | action | 60      | adventure | -1         | ApplicationDomainException | The movie run time needs to be a positive number. |

  Rule: As an Admin (but not as a User), I can delete movies.

    @txn
    Scenario Outline: Delete an existing movie
      Given the "<genre>" movie "<title>", lasting -<runtime>- minutes
      When I attempt to delete the movie "<title>"
      #TODO should I have these @spy instead?
      Then the movie "<title>" does not exist

      Examples:
        | title | genre  | runtime |
        | Hank  | action | 15      |

    @txn
    Scenario Outline: Attempt to delete a non-existing movie
      Given the movie "<title>" does not exist
      When I attempt to delete the movie "<title>"
      Then I should receive an <exception> with the message "<errorMessage>"

      Examples:
        | title  | exception                  | errorMessage                   |
        | Hank | ApplicationDomainException | The movie Hank does not exist. |

  Rule: As a any kind of user, I can list movies.

    @txn
    Scenario: List all movies
      Given the "action" movie "MovieA", lasting -60- minutes
      And the "adventure" movie "MovieB", lasting -70- minutes
      When I request a list of all movies
      Then I should receive a list of all the movies with the titles "MovieA, MovieB"

    @txn
    Scenario: List movies when no movies exist
      Given there are no movies in the system
      When I request a list of all movies
      Then I should receive an empty movie list