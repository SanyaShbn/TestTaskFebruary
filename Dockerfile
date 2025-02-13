FROM openjdk:17-jdk-slim

LABEL authors="shubi"

WORKDIR /app

COPY target/TestTaskFebruary-0.0.1-SNAPSHOT.war app.war

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.war"]