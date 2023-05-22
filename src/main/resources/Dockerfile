FROM openjdk:8-jdk-alpine

WORKDIR /app

COPY target/Hub-0.0.1-SNAPSHOT.war /app/Hub-0.0.1-SNAPSHOT.war
COPY application.properties /app/application.properties

EXPOSE 8080

CMD ["java", "-jar", "/app/Hub-0.0.1-SNAPSHOT.war"]