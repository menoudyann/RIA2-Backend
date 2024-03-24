# STAGE : Development
FROM eclipse-temurin:21 as development
WORKDIR /app
COPY pom.xml ./
COPY src ./src
RUN apt-get update && apt-get install -y maven
RUN mvn clean install -DskipTests

# STAGE : Production
FROM eclipse-temurin:21-jre as production
WORKDIR /app
COPY --from=development /app/target/*.jar app.jar

CMD ["java", "-jar", "app.jar"]