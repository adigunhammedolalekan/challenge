FROM openjdk:21

COPY build/libs/*.jar round-and-save-app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "round-and-save-app.jar"]
