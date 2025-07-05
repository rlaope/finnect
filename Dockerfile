FROM gradle:8.3-jdk17 AS builder

WORKDIR /app
COPY . .

RUN gradle clean shadowJar

FROM eclipse-temurin:8-jre

WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8583
ENTRYPOINT ["java", "-jar", "app.jar"]