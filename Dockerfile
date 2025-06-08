# -------- Stage 1: Build --------
FROM maven:3.9.6-eclipse-temurin-21 as builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests -Dspring.profiles.active=dev

# -------- Stage 2: Run --------
FROM openjdk:21-jdk-slim
ENV PORT=8080
ENV SPRING_PROFILES_ACTIVE=dev
EXPOSE 8080

COPY --from=builder /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
