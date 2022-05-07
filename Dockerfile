FROM openjdk:8-jdk-slim
COPY /home/runner/work/smartschooling-backend/smartschooling-backend/target/demo-0.0.1-SNAPSHOT.jar demo-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","demo-0.0.1-SNAPSHOT.jar"]
