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

While still in early stages of what the platform will turn into, this is an important step in incorporating secuirty measures and authentication as a means to make a more roubst application.

Est. Time Worked - 5-6 hours


### Week 10




### Week 11




### Week 12




### Week 13




### Week 14




### Week 15




### Week 16





