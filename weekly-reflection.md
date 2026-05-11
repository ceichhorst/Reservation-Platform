## Weekly Reflection

- The purpose of this page is to track progress made for the Reservation Platform Individual Project, providing a general overview of how things are going/have gone:



### Week 3

2/10/26 - Even though the semester has presented some technology barriers I am continuing to work through, developing the initial plan, concept, and overall direction for this project has been rewarding to me. It feels like I am not just completing an assignment (even at this stage), but axtually attempting to create a potential solution to a real-world problem that I have personally experienced. That connection to the problem has made the planning process more meanigful and motivating.

Writing the user stories in particular felt especially valuable - they were based around years of feedback shared while helping at the diner. I've listened to customers express frustration, and heard admins discuss what works, what doesn't, and what they wish the system could do better. Drawing from those real conversations made the user stories feel authentic and grounded in actual needs rather than assumptions.

Reviewing comments and suggestions from both customers and staff also helped me think critically about usability and practicality. From the customer who wants a quick and simple reservation process, to the manager/admin who wants accurate tracking and fewer operational headaches, it really had me thinking about different user-centeric perspectives. The wireframe layouts felt a little tricky to really pinpoint what could be the most useful and impactful for user experience, but at this point the awareness of it I feel will significantly impact the design process along the road, aiming to result in the best overall experience.

One thing I am curious (and a bit concerned) about is how I will effectively test the system, particularly regarding the primary issue of overbooking caused by race conditions. The current reservation system struggles when a high volume of users attempt to book at the same time. Replicating that high traffic scenario in this class project setting is well... a challenge to hae happen. It has me question on how to properly test whether my proposed solution truly prevents conflicts. Further research into strategies for handling concurrency will be an important next step.

Overall, Week 3 has reinforced that thoughtful preparation is crucial. Even with the challenges, I am excited to continue working towards something that addresses a problem I have seen firsthand and to see how my ideas evolve as the project progresses.


### Week 4

2/20/26 - After FINALLY gaining full access to the required programs and environment setup today, this truly felt like the beginning of development for the Reservation Platform. Up until now, much of my effort had gone into planning, research, and conceptualization. Being able to work fully within IntelliJ and Maven allowed me to transition from ideas into structure.

Because I want this project to reflect strong foundational understanding, I made a conscious decision to focus first on the exercises before diving too far into custom development. Familiarizing myself with Hibernate, Maven, and the project structure setup felt critical to long-term success.

One task I noted in my Project Plan checklist was to design the initial database schema. Drawing from my real-world experience with the diner I currently work with and their current system, I mapped out tables that would not only support basic reservations but also allow administrative flexibility. That's where the service_instance and service_template tables came into play.

The current system I work with only handles lunch reservations, with a fixed time but varying dates. I wanted this platform to allow more flexibility, potentially supporting dinner services, special events, or configurable time slots. Designing the schema forced me to think carefully about one-to-many relationships, foreign keys, capacity tracking, and administrative customization. Honestly doing this surprised me by how much I retained from the MySQL Database Programming course I took a few semesters back (albeit a few freshers), which showed me how much more confident I could reason through relational structure than I anticipated.

Not everything I set in the checklist was completed yet, but hitting that starting point was substantial for me in getting that architectural groundwork going, giving me a solid foundation to begin with.


### Week 5

2/26/26 - This week really had me focus further on dynamic system behavior compared to static structure (after catching up on previously missed materials). 

For example, I began exploring methods for handling concurrency, as one of my earliest concerns was preventing overbooking during high-traffic reservation attempts as I had experience with the diner I work with. This led me to dive into Hibernate documentation, eventually discovering a mechanism called that really caught my eye on sharing, 'LockMode'; specifically, 'PESSIMISTIC_WRITE' (https://docs.hibernate.org/orm/6.6/javadocs/org/hibernate/LockMode.html). To my understanding, this has the ability to prevent other transactions from reading, updating, deleting data within it until the current transaction completes, which to me had me think about race conditions and possibly being a way to hinder them.

While it had me thinking about if I should even try to include this in the initial designs for the application, I feel I will never truly know unless I try it out. Researching further on it had me feel this went beyond CRUD operations and like a potential solution to the solving one of the main problems I aim to design this for.

Another example includes adding a HibernateUtil class to centralize SessionFactory management. I remembered how valuable a util class could be for a Java application from last semester, so adding this felt like a way to reinforce the importance of separation of concerns and reusable configuration logic.

From all this (and more), I feel a clearer vision is coming forth for how the system could function under real-world conditions rather than just in ideal scenarios.



### Week 6

3/3/26 - This week challenged me more than I expected - especially in terms of deployment and debugging.

Something I want to note here first - I tend to hyperfocus on a task given in the course website or Brightspace until it's completed, giving me a case of tunnel vision in some cases when working on the prokect and then going back to parts I've missed or just needed to restructure better. Nevertheless, having those tasks really helps me push myself to complete obstacles in the most effective way possible I feel.

I shared a lot in my reflection for the Week 6 exercise when deploying my application through AWS, specifically noting the amount of time it took to connect to RDS with my database. Getting the application to deploy AND correctly retrieve/display data from the database needed careful amounts of time put into debugging for me.

I encountered HTTP 500 errors during deployment, which initially felt overwhelming. However, working through Elastic Beanstalk logs and tracing the issue back to a database column mismatch was valuable to take away from it. It reinforced how precise mappins must be, even small inconsistencies can break an application (another reminder to me throughout the years how the smallest details can make or break things).

During this time, I also revisited and improved my HibernateUtil configuration. Initially, it instantiated the SessionFactory statically at class load time, but it limited flexibility on how to control how it was initialized, particularly for a testing environment where I needed to utilize a different configuration. From this, I modified the class to allow external configuration via a setSessionfactory() method and throw an explicit exception if accessed before initialization, which benefitted the unit test by avoiding static initialization side effects.

Another portion of work for me was improving logging and exception handling. In Advanced Java, I sometimes held off implemented proper exception handling until later stages. With there being more to incorporate into this application , I wanted to make sure I started integrating Log4J and structured exception handling earlier in the development process, helping with clearer debugging during deployment.

From all this, I really began to realize even more on how interconnected ear layer of the application is. A simple oversight can really highlight how every component must be present for the system to function correctly.

Despite the challenges I faced, this period helped further my knowledge and experience in cloud deployment, production debugging, schema validation, and infrastructure configuration. This project really is starting to feel less and less like an assignment and more like a real-world application.


### Week 7

3/8/26 - A signifcant portion of my time was spent expanding functionality of the ReservationDao by adding methods such as getAll, getByStatus, and update. These additions were important for supporting both administrative features and more dynamic reservation management within the system.

Alongside this, I refactored the ReservationDaoTest to include a more structured setup and expanded it with additonal CRUD unit tests. This aimed to help esnure the new DAO methods were functioning correctly and reinforced the importance of thorough testing when working with database operations.

I also addressed configuration and mapping issues that were impacting the application. Adding @column annotations in Reservation improved flow and accuracy, which further showed me how small configuration errors can have a significant impact on application behavior (as any small details could make such a big impact as well).

Overall, this week helped me solidify my understanding of how the persistence layer, configuration, and testing all work together. It also emphasized the importance of clean structure and proper setup, which I feel will aid in diminishing larger issues along the road.

Est. Time Worked - 4-5 hrs


### Week 8 / Spring Break

3/20/26 - While the focus of this project should be on the application itself and the progress being made to build and complete it, I'd like to document a major barrier that had started during this week, which I feel could translate into a real world scenario somewhat, as well as troubleshooting skills. Plus, this should be documented so that if others are facing such or could potentially face it in the future, it can be addressed accordingly.

This device/VM on the school laptop I am working on for this project is really testing my patience, time management, and overall perserverence. There have been hiccups I have really started to face during this week with the capabilities of the Ubutnu VM I am limited with working on at this time. These have added additional hours of time onto my already stringint workload, espeically since the issues seem to keep reappearing. Normally this is something I feel I would bring up sooner to see if other solutions could be found, but honestly with being near the finish line with this program and knowing I don't really have any other option to work on this with, I'm just not sure if it truly is worth it - I just only wish I had the means to get a personal laptop of my own which feels liek the only way to remedy this.

The major issue I faced this week numerous times was startup issues with the Ubutnu VM. The VM would say I has no disk space to startup the instance, even though I swear it still had at least a couple gigabytes to spare. Ending down what felt like a long-winded rabbit hole, I learned how to implement commands in the BIOS to remove data from the VM so that I can actually start it up, which was not something I expected to learn how to do - there were apparently older versions of programs on the device that were disabled and taking up space that I had no clue about. The first time I did this, it freed up probably 3.5 - 4 gb of space; however, it seemed the device either keeps downloading either some kinds of updates or something else that quickly takes up space again and again. I've seen the update windows come up upon startup of the device and I do not accept them, yet somehow space keeps getting taken up. I feel I've had to free up now each time I startup the device, either on the main screen or in the BIOS. It has even interfered when I try to run my application in IntelliJ, bringing up an error message in the logs saying something along the lines of 'No space available to start the application'. I do not know how it keeps taking up so much space so quickly, so if there is any insight that can be shared here on that, I would greatly appreciate it.

Onto the application itself now.

After reviewing the feedback provided on Checkpoint 2, it seemed it was best I revisit important concepts from previous weeks in the course. Rather than rushing into adding new features, I took the time to reinforce my understanding of proper structure and design patterns the best I could within a Java/Maven application. I felt this was important because I wanted to ensure that any additional functionality I implemented would be built ona much more solid foundation rather than something would become harder to fix later.

One of the key improvements I focused on was implementing a GenericDao (based upon Week 5 materials). I structured this by creating an abstract GenericDao class that defines common CRUD operations, and then a GenericDaoImpl class provides concrete implementation of those methods. From there, the specific DAO classes for each entity in my application extend the implementation.

This helped significantly improve the organization of my code by reducing duplication of code across multiple DAOs and centralizing code to promote reuse. It also helped enforce consistency in database handling, which will pay off in the long run as the application continues to grow. Through this, it helped me gain even better understanding of abstraction and how design patterns can simplify development while improving maintainability.

Another improvement I implemented was the use of a cleandb.sql file wihtin my ReservationDaoTest. By integrating a method that reliably resets the databse to a known state before each test, I was able to make my testing process more efficient.

After addressing the feedback from Checkpoint #2 to the best of my abilities, I began expanding the functionality of the application by adding additional servlets that I anticipate will play important roles in the final system:

- AdminDashboardServlet: Designed to serve as a centralized interface for administrative functionality. While still just an 'initial build', this servlet will eventually allow administrators to manage reservations and oversee system activity.
- LogoutServlet: Implemented to properly handle session invalidation and improve application security.
- ReservationManagementServlet: Intended to handle the core functionality of the application: creating, updating, and managing reservations (still under development)

Despite the ongoing disk space issues, I feel I was still able to make meaningful progress on the application itself. However, these technical limitations did slow down development and caused disruptions with my workflow. Moving forward, I plan to continue refining existing components while also beginning to more directly address concurrency handling within the reservation system, which is the primary focus of this project.

Est. Time Worked - 12+ hrs


### Week 9

3/27/26 - Kicking off work right after spring break really took up a lot more time than I thought for my schedule, but it had me notice something during it - AWS costs.

Something I thought I had more control of last week that I didn't mention but ended up making a significant shift for me this week was AWS deployment and usage of components from it. Elastic Beanstalk, RDS, and more ended up racking up costs for me even after suspending or deleteing non-necessary elements - I couldn't find solutions to stop the costs so I decided to scrap it all and focus on building the application locally - Cognito is the exception being implemented.

With having to go back to Week 5 for previous parts, I thought it would be worth reviewing Week 7 again to make sure I aimed to stick closely to what was taught to us. The Cognito implementation I used follows very closely to the exercise previously done, including most of the classes used for it to ensure a strong and consistent workflow. One hiccup along the way included incorrect annotation of @WebServlet in a Login servlet I utilizied - it was originally '/logIn' when other files/JSPs has '/login' used, something I missed and found out way past than I should've noticed. Changes in it, alongside Auth, and AdminDashboardServlet ensured I had reliable user routing and session handling in the application.

While still in early stages of what the platform will turn into, this is an important step in incorporating security measures and authentication as a means to make a more robust application.

Est. Time Worked - 5-6 hours


### Week 10

4/5/26 - For this week, I chose to focus further on the design aspect of the program - up until this point. my focus on centered heavily on persistence, authentication, and backend functionality. I still had been working with a very rough layout/design of the home page and felt getting down the overall idea of the user experience and structure would be crucial to work on further. The importance of usability and presentation really do help make the system feel much more complete and intuitive to users than what I originally had.

I spent a considerable amount of time restructuring the home page layout, adding a main CSS file to be used across all pages, and adding reusable JSP components like a head and footer. This in turn helped me plot out how to design and keep a flow going into the reservation-details.jsp and the confirm-reservation.jsp. Seeing visually how the information between servlets/views were being passed after working these up really made the platform feel like an actual web program was coming together.

While working on the design/layout aspects more during this time, I did put time into plotting out how I felt it would be best to have aid in handling reservations. I worked up a ReservationController and custom filter methods, as a means to help assist the admin side of the platform in case admins needed to edit/handle reservations made in the system for their associated restaurant. This feels like a strong route to go about with this, but I am keeping my options open.

Est. Time Worked - 5-6 Hours


### Week 11

4/12/26 - It's interesting to see how workflow can be affected/changed/etc when working on more than one project during a certain period (currently working between a Team Project for a Vintage Arcade Finder API and my indie project Reservation Platform). It helps working with a team where tasks can be delegated, but it can become an exercise in time management very quickly.

Nevertheless, one of my main goals during this week was to refactor my GenericDao into a much more maintainable and usable class. I had previously added an abstract class (GenericDaoImpl) to help implement the GenericDao, but after reviewing feedback from my instructor during the last checkpoint, I see that I was making things much more complex than needed to be. Refactoring it to keep it simple (like how we learned in class) I feel helped lean the program towards utilizing explicit behavior more so and helping queries stay local - I ended up learning a bit about Predicate (https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/function/Predicate.html) during some research further on GenericDaos and feel this is something that will become handy down the road.

Working on the GenericDao had me refactored and rethink elements in the DAOs I currently had as well. For example, in ReservationDao, I originally had it call in Reservation variable but felt it better to call a Reservation object instead, making it much more maintainable for future development.

I also continued developing the main user side servlet (ReservationServlet) to keep the flow going for the program. I wanted to give a good starting point for it, as I am sure this will likely expand down the road (more variables, filtering, validation, etc), so working on this helped make a strong foundation from where to go for future servlets.

Balancing between this work alongside the Team Project helped make me think about how to approach things in the future for this platform - there still is a ways to go but it'll get to that point it needs to sooner than later I feel!

Est. Time Worked - 5-6 Hours


### Week 12

4/19/26 - The workload this week was relatively minimal compared to previous weeks due to the time commitment put into ensuring the Team Project adhere to its criteria and was submitted before the deadline hit. Some work was still made during this time though.

With thinking about the main user side reservation process primarily recently, I wanted to ensure I added elements that were meant for the back-end/admin side more. This included adding an AuthFilter class to check if an admin is logged in when trying to view authorized pages, and adding a LogoutServlet and adding properties to the cognito.properties file so logout is handled accordingly. I referenced much from the Auth class added but wanted to make sure I had a better understanding of those elements, including the java.net package (https://docs.oracle.com/javase/8/docs/api/java/net/package-summary.html) - understanding elements that helped handle networking processes really had me think about how such could be scaled up down the line for more complex systems.

One thing I will note is I came across a hiccup this week - my IntelliJ project was not able to push to my GitHub repo for some reason. I kept coming getting fatal error message popping up when trying to to a 'git push'. I delved further onto GitHub community pages, and it seems what I encountered indicated the connection to my repo and IntelliJ project was broken somehow. I was not able to find out what caused it but it was recommended to make a backup copy and then connect that one to the repo - the process thankfully didn't take too long but it was such an odd thing to occur. I wonder if the issues I've been experiencing with the disk space pileups had any factor towards this.

With getting more time back to focus on the for the coming weeks, this feels like things are going to pick up here much more very soon.

Est. Time - 4 hours


### Week 13

4/26/26 - Came across a hiccup this week - something caused my project to not push to my GitHub repo. I kept getting critical fatal errors when trying to  This week really focused on refining reservation workflows, improving business logic separation, and continuing to evolve the application the best that I can with what I have learned and am continuing to learn about in the class and through my own research.

One portion I felt was important during this time was implementing service layers, ReservationService, ReservationResult, DayAvailability, and AvailabilityService - up until now, reservation validation and transactional logic were spread across servlet and DAO methods, which seemed to have some errors being encountered come up along the way. Creating these layers I feel helped centralize reservation-specific business logic and better separated responsibilities between presentation layer, business, layer, and persistence layer.

For DayAvailability and AvaiabilityService, these helped shape the availability calendar I implemented on the home page of the platform, as a way for users to quickly view what is available and what is full (ie. total seats vs booked seats for a service date). For ReservationService and ReservationResult, these were implemented to add in creating and confirming the reservation instead of placing all that logic into the ReservationServlet and ConfirmationServlet. This would then allow easier scalability if there are any needs to provide further functionality down the road. For example, I know I am wanting to still see if there is a way to provide a confirmation email to those who successfully submit and confirm a reservation, which the ReservationService class could help implement if a usable method is worth implementing. There were some back and forth moments on the best way to ensure things were working properly that took a considerable amount of time but getting to assess the practices done and refactor when necessary to improve the program was beneficial during this week for me.

With my focus on concurrency during this project, a question that comes to mind for me during all this is how can I best ensure I am implementing elements that handle concurrency properly in this. I've delved into PESSIMISTIC_WRITE already, but have researched further on retry mechanisms (information on 'Retry': https://docs.oracle.com/middleware/1213/core/ASWSJ/oracle/webservices/annotations/async/Retry.html), deadlock detection, and other Java concurrency-focused utilities (ExecutorService, Lock) (https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/package-summary.html). This is something I am continuing to research further about at this point before I start implementing elements further.

It feels great to see more come together for this and am looking forward to where more of it will go.

Est. Time - 10 hours


### Week 14




### Week 15




### Week 16


