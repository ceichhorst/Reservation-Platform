## Project Plan



This project plan outlines the week-by-week tasks for designing, implementing, testing, and deploying an enterprise-level Java-based reservation platform focused on fairness, concurrency, and usability.



### Week 3

* \[ ] Clearly identify MVP vs non-MVP user stories
* \[ ] Write detailed project plan
* \[ ] Design initial application architecture
* \[ ] Research authentication provider (AWS Cognito)
* \[ ] Research possible external web services/APIs
* \[ ] Create initial screen designs and application flow diagrams
* \[ ] Update Weekly Reflection



\*\*Checkpoint 1 Due 2/11: Problem statement, user stories, project plan, screen designs, and reflection pushed to GitHub.



### Week 4



Focus User Story: Submit Reservation (Guest)

* \[ ] Design initial database schema

  * Reservation
  * Service (Date/Time offering)
  * Administrator

* \[ ] Identify one-to-many relationships (Service -> Reservations)
* \[ ] Create development database
* \[ ] Configure Hibernate/JPA settings
* \[ ] Create DAO for Reservation with basic CRUD operations
* \[ ] Create test database configuration
* \[ ] Begin writing unit tests for Reservation DAO
* \[ ] Weekly Reflection



### Week 5



Focus User Story: View Available Services \& Prevent Overbooking

* \[ ] Implement DAO methods for reading available services
* \[ ] Add capacity and party-size validation logic
* \[ ] Implement transactional reservation creation logic
* \[ ] Research and prototype concurrency-handling strategies
* \[ ] Expand unit tests to cover validation and failure scenarios
* \[ ] Begin implementing Log4J logging framework
* \[ ] Weekly Reflection



### Week 6



Focus User Story: Administrative Reservation Oversight

* \[ ] Create DAO for Service entity will full CRUD
* \[ ] Add unit tests for Service DAO
* \[ ] Implement administrator-facing reservation queries
* \[ ] Log reservation attempts, successes, and failures
* \[ ] Refactor code for clarity and maintainability
* \[ ] Weekly Reflection



### Week 7



* \[ ] Verify Checkpoint 2 requirements are complete and visible in GitHub
* \[ ] Clean up database schema and entity mappings
* \[ ] Improve test coverage where needed
* \[ ] Begin authentication setup for administrator
* \[ ] Create AWS RDS database instance
* \[ ] Update application configuration for AWS database
* \[ ] Deploy initial version of application to AWS
* \[ ] Add deployed link to student indie project list
* \[ ] Weekly Reflection



\*\*Checkpoint 2 Due 3/4: Database designed and created, at least one DAO with full CRUD implemented using Hibernate, DAO fully unit tested, Log4J implemented



### Week 8



Focus User Story: Admin Sign In \& Reservation Management

* \[ ] Complete authentication and authorization for administrators
* \[ ] Restrict administrative features to authenticated users
* \[ ] Implement admin JSPs for viewing reservations by service
* \[ ] Implement controller logic for admin workflows
* \[ ] Validate authentication and authorization flows
* \[ ] Weekly Reflection



### Week 9



* \[ ] Double-check all Checkpoint 3 requirements
* \[ ] Verify AWS deployment stability
* \[ ] Confirm JSPs render live database data
* \[ ] Add deployed application link to GitHub and student repo
* \[ ] Address any feedback from checkpoint review
* \[ ] Weekly Reflection



\*\*Checkpoint 3 Due 3/25: Application deployed to AWS, authentication implemented, at least one JSP displays data from the database



### Week 10



Focus User Story: Usability \& Accessibility Improvements

* \[ ] Improve reservation form usability and clarity
* \[ ] Add clear sold-out and status messaging
* \[ ] Improve error handling and validation messages
* \[ ] Test application under simulated high-traffic conditions
* \[ ] Refine UI layout for accessibility and readability
* \[ ] Weekly Reflection



### Week 11



Focus User Story: External Service Integration

* \[ ] Select external web service or public API
* \[ ] Implement Java-based service integration
* \[ ] Add logging around external service calls
* \[ ] Handle service failure scenarios gracefully
* \[ ] Add unit tests where applicable
* \[ ] Weekly Reflection



### Week 12



* \[ ] Refactor code based on testing and feedback
* \[ ] Improve documentation and JavaDoc
* \[ ] Increase unit test coverage
* \[ ] Review project against rubric for gaps
* \[ ] Weekly Reflection



### Week 13



* \[ ] Conduct peer design/code review
* \[ ] Incorporate peer and instructor feedback
* \[ ] Improve concurrency handling if needed
* \[ ] Finalize non-MVP features if time allows
* \[ ] Weekly Reflection



### Week 14



* \[ ] Prepare presentation outline and talking point
* \[ ] Review application architecture and technical decisions
* \[ ] Perform end-to-end testing
* \[ ] Final UI polish
* \[ ] Weekly Reflection



### Week 15



* \[ ] Implement feedback from Week 14 review
* \[ ] Finalize documentation (README, architecture explanation, etc)
* \[ ] Record video demonstration
* \[ ] Add video link to GitHub README
* \[ ] Perform code quality and cleanup pass
* \[ ] Weekly Reflection



### Week 16



* \[ ] Final verification against rubric
* \[ ] Final touches and bug fixes
* \[ ] Ensure documentation is complete and accurate
* \[ ] Final Reflection



Individual Project Deadline: 5/13





\*\*\*Note: This project plan is subject to change as requirements are refined and feedback is incorporated.


