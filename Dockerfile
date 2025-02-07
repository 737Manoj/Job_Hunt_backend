# Use an official OpenJDK runtime as a parent image
FROM openjdk:19-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the project files into the container
COPY . .

# Give execution permission to mvnw
RUN chmod +x mvnw

# Build the application (for Maven projects)
RUN ./mvnw clean package -DskipTests

# Expose the application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/Job-Portal-Application.jar"]
