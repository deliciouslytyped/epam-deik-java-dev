#TODO
Feature: Admin and user can CRUD in their respective areas

  Scenario: Split initialization output hack (intellij)

  Scenario: Admin can add data
    Given the admin user can log in with the admin user credentials
    Then admin can add movie row
    Then admin can add room row

  Scenario: Admin can delete data
    Given the admin user can log in with the admin user credentials
    Then admin can delete movie row
    Then admin can delete room row

  Scenario: Admin can update data
    Given the admin user can log in with the admin user credentials
    Then admin can update movie row
    Then admin can update room row