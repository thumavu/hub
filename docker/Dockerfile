FROM openjdk:17-jdk-slim

WORKDIR /hub

ENV start-class=com.microservice.hub.HubApplication

COPY Hub-0.0.1-SNAPSHOT.war .
COPY pom.xml .

RUN apt-get update && apt-get install -y maven
RUN mvn dependency:go-offline


CMD ["java", "-jar", "Hub-0.0.1-SNAPSHOT.war"]