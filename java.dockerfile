FROM openjdk:17
COPY ./target/spaceships-0.0.1-SNAPSHOT.jar /spaceships-0.0.1-SNAPSHOT.jar
CMD ["java", "-jar", "/spaceships-0.0.1-SNAPSHOT.jar"]