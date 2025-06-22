FROM openjdk:21-jdk-slim
VOLUME /tmp
COPY target/task-0.0.1-SNAPSHOT.jar task.jar
ENTRYPOINT ["java", "-jar", "/task.jar"]
