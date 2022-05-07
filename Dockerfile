FROM openjdk:8-jdk-slim
COPY /target/demo-0.0.1-SNAPSHOT.jar.jar /target/demo-0.0.1-SNAPSHOT.jar.jar
ENTRYPOINT ["java","-jar","/demo-0.0.1-SNAPSHOT.jar.jar"]
