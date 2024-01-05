#TODO well technically are we really doing anything inequivalent to setting the database time?
Feature: The database time can be mocked

  Scenario Outline: Set and unset the test time
    Then the date is not -<localtime>-
    Given we act as if the date is -<localtime>-
    Then the date is -<localtime>-

    Examples:
      | localtime                               |
      | 2024-05-20T15:30:45+02:00[Europe/Paris] |
