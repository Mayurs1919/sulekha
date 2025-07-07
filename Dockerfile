FROM eclipse-temurin:17-jdk-alpine
LABEL "language"="java"
LABEL "framework"="spring-boot"
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline
COPY . .
RUN ./mvnw clean package -DskipTests

# Debug: show JAR file presence
RUN ls -al target

EXPOSE 8080
CMD ["java", "-jar", "target/whbrd-0.0.1-SNAPSHOT.jar"]
