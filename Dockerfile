FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /spaceships
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /spaceships
COPY --from=build /spaceships/target/spaceships-0.0.1.jar spaceships-0.0.1.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "spaceships-0.0.1.jar"]
