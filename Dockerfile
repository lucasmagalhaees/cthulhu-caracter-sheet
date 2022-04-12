# Compile and build the project
FROM gradle:jdk11 as build-image

VOLUME /tmp
WORKDIR /
ADD . .


RUN gradle --stacktrace clean test build
COPY build/libs/*.jar app.jar

FROM openjdk:11

#
# Copy the jar file in and name it app.jar.
#
COPY --from=build-image app.jar /

#
# The command to run when the container starts.
#
ENTRYPOINT ["java", "-jar", "app.jar"]