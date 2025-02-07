# Use an official OpenJDK runtime as a parent image
FROM openjdk:19-jdk

 
# Copy the built jar file into the container
COPY target/Job-*.jar /Job-Portal-Application.jar

# Expose the application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "/Job-Portal-Application.jar"]