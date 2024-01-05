Feature: Account management in the ticket service application

  Scenario Outline: admin and normal users can log in
  Scenario Outline: admin and normal users can log out
  Scenario Outline: admin and normal users can describe their account

  #TODO is this specced?
  Scenario Outline: Admin and normal users can be created listed updated and deleted by admin users when logged in
  Scenario Outline: Admin and normal users cannot be created listed updated and deleted by normal users neither when logged in
  Scenario Outline: logged out users cannot do anything

  Scenario Outline: admin/user commands are only available to logged in admin user/user user