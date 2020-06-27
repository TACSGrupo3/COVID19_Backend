FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY /src/main/resources/data/data.json /data/data.json
RUN apk --update add fontconfig ttf-dejavu
ENTRYPOINT ["java","-jar","/app.jar"]