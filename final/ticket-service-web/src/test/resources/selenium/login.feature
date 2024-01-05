#TODO
Feature: Admin and user authenticated areas are authenticated, public area is not

  Scenario: Split initialization output hack (intellij)

  # TODO just shove selenium ide tests into blobs due to time constraints
  Scenario: The home page is accessible
    Then the home page is accessible

  Scenario: Static resources are accessible
    Then static resources are accessible

  Scenario: Admin login works correctly
    Then the admin user can log in with the admin user credentials
    Then the admin user can not log in with the normal user credentials

  Scenario: User login works correctly
    Then the user can log in with the normal user credentials
    Then the user can not log in with the admin user credentials