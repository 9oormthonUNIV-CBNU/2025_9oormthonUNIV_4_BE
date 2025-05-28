FROM eclipse-temurin:21-jdk
WORKDIR /app

COPY .env .env
COPY build/libs/*.jar app.jar

ENV JAVA_OPTS=""
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.import=optional:file:.env[.properties]"]
EXPOSE 8080