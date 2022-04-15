# Compile and build the project
FROM gradle:jdk11 as build-image
RUN mkdir -p /app
COPY . /app
WORKDIR /app
RUN gradle --stacktrace --no-daemon clean build

FROM openjdk:11
COPY --from=build-image /app/build/libs/*.jar /app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]