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




### Week 8




### Week 9




### Week 10




### Week 11




### Week 12




### Week 13




### Week 14




### Week 15




### Week 16





