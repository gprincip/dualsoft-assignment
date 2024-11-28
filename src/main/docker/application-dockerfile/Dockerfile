
# Use an official Java runtime as a parent image
FROM openjdk:23

# Set the working directory
WORKDIR /app

# Copy the application JAR file
COPY ./assignment-0.0.1-SNAPSHOT.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]