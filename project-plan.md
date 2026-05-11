## Project Plan



This project plan outlines the week-by-week tasks for designing, implementing, testing, and deploying an enterprise-level Java-based reservation platform focused on fairness, concurrency, and usability.



### Week 3

* \[X] Clearly identify MVP vs non-MVP user stories
* \[X] Write detailed project plan
* \[X] Design initial application architecture
* \[X] Research authentication provider (AWS Cognito)
* \[X] Research possible external web services/APIs
* \[X] Create initial screen designs and application flow diagrams
* \[X] Update Weekly Reflection



\*\*Checkpoint 1 Due 2/11: Problem statement, user stories, project plan, screen designs, and reflection pushed to GitHub.



### Week 4



Focus User Story: Submit Reservation (Guest)

* \[X] Design initial database schema

  * Reservation
  * Service (Date/Time offering)
  * Administrator

* \[X] Identify one-to-many relationships (Service -> Reservations)
* \[X] Create development database
* \[X] Configure Hibernate/JPA settings
* \[X] Create DAO for Reservation with basic CRUD operations
* \[X] Create test database configuration
* \[X] Begin writing unit tests for Reservation DAO
* \[X] Weekly Reflection



### Week 5



Focus User Story: View Available Services \& Prevent Overbooking

* \[X] Implement DAO methods for reading available services
* \[X] Add capacity and party-size validation logic
* \[X] Implement transactional reservation creation logic
* \[X] Research and prototype concurrency-handling strategies
* \[X] Begin making unit test(s) to cover validation and failure scenarios
* \[X] Begin implementing Log4J logging framework
* \[X] Weekly Reflection



### Week 6



Focus User Story: Administrative Reservation Oversight

* \[X] Create DAO for Reservation entity will full CRUD
* \[X] Add unit tests for Reservation DAO
* \[ ] Implement administrator-facing reservation queries
* \[ ] Log reservation attempts, successes, and failures [Not yet]
* \[X] Refactor code for clarity and maintainability
* \[X] Weekly Reflection



### Week 7



* \[X] Verify Checkpoint 2 requirements are complete and visible in GitHub
* \[X] Clean up database schema and entity mappings
* \[X] Improve test coverage where needed
* \[X] Begin authentication setup for administrator
* \[X] Create AWS RDS database instance
* \[X] Update application configuration for AWS database
* \[X] Deploy initial version of application to AWS
* \[X] Add deployed link to student indie project list
* \[X] Weekly Reflection



\*\*Checkpoint 2 Due 3/4: Database designed and created, at least one DAO with full CRUD implemented using Hibernate, DAO fully unit tested, Log4J implemented



### Week 8



Focus User Story: Admin Sign In \& Reservation Management

* \[X] Complete authentication and authorization for administrators
* \[X] Restrict administrative features to authenticated users [Only through the admin dashboard at the moment]
* \[X] Implement admin JSPs for viewing reservations by service
* \[X] Implement controller logic for admin workflows
* \[X] Validate authentication and authorization flows
* \[X] Weekly Reflection



### Week 9



* \[X] Double-check all Checkpoint 3 requirements
* \[X] Verify AWS deployment stability
* \[X] Confirm JSPs render live database data
* \[X] Add deployed application link to GitHub and student repo
* \[X] Address any feedback from checkpoint review
* \[X] Weekly Reflection



\*\*Checkpoint 3 Due 3/25: Application deployed to AWS, authentication implemented, at least one JSP displays data from the database



### Week 10



Focus User Story: Usability \& Accessibility Improvements

* \[X] Improve reservation form usability and clarity (continuously reviewed)
* \[X] Refine UI layout for accessibility and readability (continuously)
* \[X] Weekly Reflection



### Week 11



Focus User Story: External Service Integration

* \[X] Select external web service or public API (Javamail)
* \[X] Refactor GenericDao implementation
* \[X] Expand data/servlet handling for reservation process
* \[X] Add unit tests where applicable
* \[X] Weekly Reflection



### Week 12



* \[X] Refactor code based on testing and feedback
* \[X] Improve documentation and JavaDoc
* \[X] Increase unit test coverage
* \[X] Implement admin authentication check (AuthFilter)
* \[X] Weekly Reflection



### Week 13


* \[X] Implement service layers/DTOs to centralize logic
* \[ ] Incorporate peer and instructor feedback
* \[X] Continue research and developing plan for proper implementation of concurrency handling if needed
* \[ ] Finalize non-MVP features if time allows
* \[X] Weekly Reflection



### Week 14



* \[ ] Prepare presentation outline and talking point
* \[ ] Review application architecture and technical decisions
* \[ ] Perform end-to-end testing
* \[ ] Final UI polish
* \[ ] Weekly Reflection



### Week 15


* \[X] Conduct peer design/code review
* \[ ] Implement feedback from Week 14 review
* \[ ] Finalize documentation (README, architecture explanation, etc)
* \[ ] Record video demonstration
* \[ ] Add video link to GitHub README
* \[ ] Perform code quality and cleanup pass
* \[ ] Finalize Unit Tests/Handling
* \[ ] Weekly Reflection



### Week 16



* \[ ] Final verification against rubric
* \[ ] Final touches and bug fixes
* \[ ] Ensure documentation is complete and accurate
* \[ ] Final Reflection



Individual Project Deadline: 5/13





\*\*\*Note: This project plan is subject to change as requirements are refined and feedback is incorporated.




