FROM maven:3.9.1-eclipse-temurin-17-alpine AS base
WORKDIR /workspace
COPY pom.xml /workspace/pom.xml
COPY /api/src /workspace/api/src
COPY /api/pom.xml /workspace/api/pom.xml
COPY /service/src /workspace/service/src
COPY /service/pom.xml /workspace/service/pom.xml
RUN mvn clean package -Dmaven.test.skip=true

FROM eclipse-temurin:17.0.7_7-jre-ubi9-minimal
WORKDIR /opt/app
RUN mkdir config
RUN mkdir logs
COPY --from=base /workspace/service/target/*.jar app.jar
VOLUME /opt/app/logs
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=deploy", "app.jar"]