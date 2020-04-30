FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY /src/main/resources/data/data.json /data/data.json
ENTRYPOINT ["java","-jar","/app.jar"]