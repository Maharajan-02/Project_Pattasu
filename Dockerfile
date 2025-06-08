# Use a slim Java 21 image
FROM openjdk:21-jdk-slim

# Set environment port (Render uses PORT variable)
ENV PORT 8080
EXPOSE 8080

# Add the JAR file to the container
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]
