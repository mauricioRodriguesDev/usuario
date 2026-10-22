FROM gradle:9-jdk17-corretto AS build
WORKDIR /app
COPY . .
RUN gradle build --no-daemon


FROM amazoncorretto:17-alpine-jdk

WORKDIR /app
COPY --from=build /app/build/libs/*.jar /app/usuario.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/usuario.jar"]