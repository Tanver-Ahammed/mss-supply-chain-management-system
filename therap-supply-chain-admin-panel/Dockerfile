# Use an official OpenJDK runtime as a base image
FROM openjdk:17-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the packaged JAR file into the container
COPY target/therap-supply-chain-admin-panel-0.0.1-SNAPSHOT.jar app.jar

# Expose the port that the application will run on
EXPOSE 8081

# Command to run the application
CMD ["java", "-jar", "app.jar"]
