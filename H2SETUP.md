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
spring.application.name=fullstack-demo

# H2 configurations
spring.h2.console.enabled=true
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=admin
spring.datasource.password=admin
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=none

# Use persistent H2 database instead of an in-memory
spring.datasource.url=jdbc:h2:file:./fullstack_demo_db;DB_CLOSE_ON_EXIT=FALSE;
```

I also had to make som adjustments to the `User()`-class since "User" is a reserved term for SQL. And I modified the
other classes `Id` needs the `jakarta.persistence.Id` for the database and `Lombok` removes boilerplate code. 

The `Lombok` `@Data` annotation, it will automatically generate getters, setters, equals(), hashCode(), and a constructor.
This satisfies the requirement for an empty constructor. This means that we do not have to worry about making the 
methods or constructors for the models.

The `@Entity` annotation tells JPA that this class represents a persistent entity that can be mapped to a database table.