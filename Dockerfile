FROM gradle:8.8.0-jdk17 AS BUILD
WORKDIR /app
COPY . .
RUN gradle build --no-daemon

FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app/api-usuario.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/api-usuario.jar"]