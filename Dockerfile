FROM openjdk:17-jdk-alpine

WORKDIR /app

copy . .

RUN apk add --no-cache maven && \
    mvn package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "/app/target/schedulingHealthNove-0.0.1-SNAPSHOT.jar"]