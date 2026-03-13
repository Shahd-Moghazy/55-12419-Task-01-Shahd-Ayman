FROM eclipse-temurin:25.0.2_10-jdk

WORKDIR /app

COPY target/shahd_ayman-0.0.1-SNAPSHOT.jar app.jar

COPY src/main/resources/notes.json /data/notes.json
COPY src/main/resources/users.json /data/users.json

ENV USER_NAME=Docker_Shahd_Ayman
ENV ID=Docker_55_12419

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]