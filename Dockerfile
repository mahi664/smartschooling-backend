FROM openjdk:8-jdk-slim
COPY /target/smart-schooling.jar /target/smart-schooling.jar
ENTRYPOINT ["java","-jar","/smart-schooling.jar"]
