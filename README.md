# Hub microservice

* The Hub acts as an interface where different banks can connect their client interfaces. 
* The Hub is responsible for connecting to Bank 1, Bank 2, and Bank 3 servers.
* The goal is to enable file transfers between the banks using the Hub as a central communication point.

## Protobuf for Message Serialization

* To help with communication between the services, we use Protobuf for message serialization.
* Protobuf provides a language-egnostic and efficient way to serializes data.
* The protobuf schema is used to generate Java classes, which are used to serialise and deserialize messages.

## Requirements
- openjdk 17
- maven
- docker and docker-compose


## To run the service execute the below commands
- mvn clean install
- java -jar target/Bank-0.0.1-SNAPSHOT.war