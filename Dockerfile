FROM openjdk:8-jre-alpine3.9
WORKDIR /
EXPOSE 8081
COPY target/tictactoe-0.0.1-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java","-jar","app.jar"]
