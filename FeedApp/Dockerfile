FROM eclipse-temurin:21
#ARG JAR_FILE=target/*.jar
RUN mkdir /opt/app
#COPY FeedApp/build/libs/FeedApp-0.0.1-SNAPSHOT.jar /opt/app
COPY src/main/resources/FeedApp-0.0.1-SNAPSHOT.jar /opt/app
ENTRYPOINT ["java","-jar","/app.jar"]