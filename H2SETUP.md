### H2 Database Set-Up

Added the `H2` dependency by adding the dependencies to the `build.gradle.kts` file:
``` 
implementation("org.springframework.boot:spring-boot-starter-data-jpa")
implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
implementation("jakarta.transaction:jakarta.transaction-api")
annotationProcessor("org.projectlombok:lombok")
compileOnly("org.projectlombok:lombok")
runtimeOnly("com.h2database:h2")
```

And I modified `applications.properties` by adding the lines
```  
spring.application.name=FeedApp

# H2 configurations
spring.h2.console.enabled=true
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=admin
spring.datasource.password=admin
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update

# Use persistent H2 database instead of an in-memory
spring.datasource.url=jdbc:h2:mem:fullstack_db

# Make sure the database matches the entity definitions (generates ID automatically and other fields not specified)
hibernate.hbm2ddl.auto=validate
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect

# Add logging to see what's happening during entity creation
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

I also had to make som adjustments to the `User()`-class since "User" is a reserved term for SQL. And I modified the
other classes so `Lombok` can remove boilerplate code. 

The `Lombok` `@Data` annotation, it will automatically generate getters, setters, equals(), hashCode(), and a constructor.
This satisfies the requirement for an empty constructor. This means that we do not have to worry about making the 
methods or constructors for the models because `Lombok` does it for us. 

The `@Entity` annotation tells JPA that this class represents a persistent entity that can be mapped to a database table.

Then I connected to the database inside `IntelliJ` by running the application and adding the data with the login: 
```
Name:           fullstack_db
Authentication: User & Password
User:           admin
Password:       admin
Database:       fullstack_db
URL:            jdbc:h2:mem:fullstack_db 
```
```
OR 
URL:jdbc:h2:file:~/fullstack_db (depending on what is configured in application.properties)
```
But the tables did not generate. They did however generate automatically when I went to 
`http://localhost:8080/h2-console` in the browser and logged in there with the same credentials. 

TODO: How to save data in database when sending POST request
TODO: Generate UUID id values
