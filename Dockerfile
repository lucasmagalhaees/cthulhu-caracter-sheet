# Compile and build the project
FROM gradle:7.6-jdk17 AS build-image
WORKDIR /app
COPY . /app
RUN gradle --no-daemon clean bootJar

# Run it. The platform gives the port in PORT (Vercel's container runtime, Heroku); 80 otherwise (the port Vercel routes to).
FROM eclipse-temurin:17-jre
COPY --from=build-image /app/build/libs/cthulhu.character.generator.jar /app.jar
ENV PROFILE=prod
EXPOSE 80
ENTRYPOINT ["sh", "-c", "java -XX:TieredStopAtLevel=1 -XX:+UseSerialGC -Xshare:auto -Dserver.port=${PORT:-80} -Dspring.profiles.active=${PROFILE} -jar /app.jar"]
