### MongoDB Set-Up 

Added `MongoDB` dependecy by adding  the dependencies to the `build.gradle.kts` file:
``` 
implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
```
And I modified `applications.properties` by adding the configurations for MongoDB.
```  
# MongoDB configurations
spring.data.mongodb.uri=mongodb://admin:admin@localhost:27017/feedapp_mongodb
spring.data.mongodb.database=feedapp_mongodb
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.authentication-database=mongodb_admin
spring.data.mongodb.username=mongodb_admin
spring.data.mongodb.password=mongodb_admin
```
Then I opened `MongoDB Compass` and created a new database called `feedapp_mongodb` with the collection `users`. 