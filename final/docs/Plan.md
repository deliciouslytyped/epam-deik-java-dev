### Plan
- [x] Bring up/test tooling (see also libs below, some prereqs) (downside of doing this all at once is that it may be misconfigured till I actually use it)
    - [x] merge this commit https://github.com/epam-deik-cooperation/epam-deik-java-dev/commit/1751e53dea6f0044597e045a5b18599f4a9dbb72
    - [x] Configured Spring JPA
      - [x] Hello world classes minimum to talk to the DB
      - [x] Can talk to postgres (spring starts successfully)
      - [x] Can talk to h2 (spring starts successfully)
    - [x] implement and init single minimal db object (room table) to check db work
        - [x] no h2 console because needs web and we dont want it, but can connect via intellij and see object
        - [x] intellij db tools work with postgres and see object 
    - [ ] Configured Spring Shell
      - [ ] TODO jani config stuff?
    - [x] Can run Postgres container
    - [x] Configured H2 and Postgres in CI and prod configurations
    - [x] (Preconfigured ✔) Configured Checkstyle
    - [x] Acceptance tests failed successfully
    - [x] Line and branch test coverage working
      - [x] surefire fails on coverage
        - `mvn clean verify`
        - needs at least one test to not get "Skipping JaCoCo execution due to missing execution data file."
      - [ ] Add the ignores specified in class, in the in-class money example:
        ```xml
        <excludes>
          <exclude>com/epam/training/webshop/Application.*</exclude>
          <exclude>com/epam/training/webshop/core/configuration/**</exclude>
          <exclude>com/epam/training/webshop/ui/configuration/**</exclude>
        </excludes>
        ```
    - [x] (Preconfigured ✔) Same for JaCoCo and Checkstyle as below?
    - [x] Start with incrementing minimum grade tests to 3 because
          this is where failing on checkstyle is first enabled.
        - [x] tests fail successfully
    - [ ] Increment minimum grade tests to 4
    - [ ] Increment minimum grade tests to 5
- [ ] Libs 
  - [x] (Preconfigured ✔) Added Spring Boot Shell
    - spring-shell-starter is in ticket-service dependencies
  - [x] (Preconfigured ✔) Added Spring Boot Starter
    - top level (ticket-service-parent) pom contains spring-boot-starter-parent
  - [x] Add Spring Boot JPA
    - added spring-boot-starter-data-jpa to dependencies in ticket-service
  - [x] Include Mockito and Assertj via spring-boot-starter-test
    - Added spring-boot-starter-test to dependencies in ticket-service
  - [x] Add Lombok (TODO wasn't there something to do after adding lombok)?
    - added to dependencies in ticket-service, copied lombok.config from example to main (dont need for tests)
  - [x] Add spring security and integration testing lib (Idea is to try to use this for the admin stuff) 
    - added spring-boot-starter-security / spring-security-test to dependencied in ticket-service
    - [ ] TODO do we actually need the test thing? 
- [ ] Do full ER design
   - [x] User, movie, room, screening, booking
   - [x] Movie, room, screening price components are aggregated to create the final price; previous bookings are unchanged.
       - Removing price components is not specified by the specification. This makes the design simpler here
         because we don't need to account for foreign key violations on deletions, though arguably this should
         be designed for.
   - [ ] constraints (nullability, unique constraints, relationships, data types), normalization (anomalies)
   - [ ] It would have helped to know how to use the ORM properly https://www.baeldung.com/jpa-many-to-many
- [ ] Code organization
    - [ ] layers
      - [ ] database access layer (Entities)
      - [ ] service layer (Repositories, used by Services)
      - [ ] shell
      - [ ] tests / mocking
    - [ ] The basic admin commands seem to follow the same scheme; create an AdminCRUD class or such
        - Movies, rooms, screenings | create, update, delete, list <>s
    - [ ] Sign up, sign in, describe behavior depends on user type
    - [ ] Price administration (update, create, attach, show)
    - [ ] User commands: do booking

### Misc
- Composition over inheritance?
- Interface/impl split
- Observer pattern? (I don't think we need this here. Proper DB design means the booking price system shouldn't 
  need propagating updates?)
- Full test database content in tests
- Integration tests
- What's the deal with these?:
  ```xml
  <!--suppress UnresolvedMavenProperty -->
  <requirements.cucumber-tags>@grade2-requirement or @grade3-requirement</requirements.cucumber-tags>
  ```