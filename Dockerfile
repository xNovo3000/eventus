FROM maven:3.9.1-amazoncorretto-17 AS base
WORKDIR /workspace
COPY pom.xml /workspace
COPY /src /workspace/src
RUN mvn package -Dmaven.test.skip=true

FROM amazoncorretto:17.0.6-al2023-headless
COPY --from=base /workspace/target/*.jar app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=deploy", "-jar", "app.jar"]