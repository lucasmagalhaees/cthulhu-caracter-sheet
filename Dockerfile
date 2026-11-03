# Compile and build the project
FROM gradle:7.6-jdk11 AS build-image
WORKDIR /app
COPY . /app
RUN gradle --no-daemon clean bootJar

# Run it. The platform gives the port in PORT (Vercel's container runtime, Heroku); 8080 otherwise.
FROM eclipse-temurin:11-jre
COPY --from=build-image /app/build/libs/cthulhu.character.generator.jar /app.jar
ENV PROFILE=prod
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8080} -Dspring.profiles.active=${PROFILE} -jar /app.jar"]
