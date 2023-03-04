FROM maven:3.8.7-amazoncorretto-17 AS base
WORKDIR /workspace
COPY pom.xml /workspace
COPY /src /workspace/src
RUN mvn clean package -Dmaven.test.skip=true

FROM amazoncorretto:17.0.6-al2022-RC-headless
COPY --from=base /workspace/target/*.jar app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=deploy", "-jar", "app.jar"]