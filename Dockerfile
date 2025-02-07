# Use an official OpenJDK runtime as a parent image
FROM openjdk:19-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the project files into the container
COPY . .

# Give execution permission to mvnw
RUN chmod +x mvnw

# Build the application
RUN ./mvnw clean package -DskipTests

# Copy the built JAR to the app directory
RUN cp target/*.jar app.jar

# Expose the application port
EXPOSE 10000

# Run the application, using the port assigned by Render
CMD ["java", "-jar", "app.jar", "--server.port=${PORT}"]
