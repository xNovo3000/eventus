FROM eclipse-temurin:17.0.7_7-jre-ubi9-minimal
WORKDIR /opt/app
RUN mkdir config
RUN mkdir logs
COPY service/target/eventus-service-*.jar app.jar
VOLUME /opt/app/logs
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=deploy", "app.jar"]
