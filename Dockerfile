FROM gradle:8.8.0-jdk17 AS BUILD
WORKDIR /app
COPY . .
RUN gradle build --no-daemon

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/api-usuario-0.0.1-SNAPSHOT.jar /app/api-usuario.jar
EXPOSE 8081
CMD ["java", "-jar", "/app/api-usuario.jar"]