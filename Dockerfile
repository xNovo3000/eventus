FROM maven:3.9.1-amazoncorretto-17-debian-bullseye AS base
WORKDIR /workspace
COPY pom.xml /workspace/pom.xml
COPY /api/src /workspace/api/src
COPY /api/pom.xml /workspace/api/pom.xml
COPY /service/src /workspace/service/src
COPY /service/pom.xml /workspace/service/pom.xml
RUN mvn clean package -Dmaven.test.skip=true

FROM amazoncorretto:17.0.6-al2023-headless
COPY --from=base /workspace/service/target/*.jar app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=deploy", "-jar", "app.jar"]