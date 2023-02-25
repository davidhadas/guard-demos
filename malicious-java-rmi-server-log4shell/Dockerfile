# Maven build container 
FROM amazoncorretto:8 as  maven_build
RUN mkdir /build/ 
WORKDIR /build

COPY pom.xml .
COPY mvnw .
RUN mkdir .mvn
COPY .mvn .mvn
RUN ./mvnw verify clean --fail-never

COPY src/ ./src/
RUN ./mvnw package
run ls -l ./target
run ls -l /build/target

#pull base image
FROM eclipse-temurin:8

#maintainer 
MAINTAINER davidhadas@gmail.com
EXPOSE 1099

#default command
CMD java -jar /data/malicious-java-rmi-1.0-SNAPSHOT.jar

COPY --from=maven_build /build/target/*.jar  /data/

RUN ls -l /data/

