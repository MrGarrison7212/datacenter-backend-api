#----------- BUILD STAGE -----------

FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

COPY src ./src
RUN mvn -q -DskipTests package

#----------- RUN STAGE -----------

FROM eclipse-temurin:21-jre
WORKDIR /app

RUN useradd -ms /bin/bash appuser
USER appuser

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENV SERVER_PORT = 8080
ENV SPRING_PROFILES_ACTIVE=h2

ENTRYPOINT ["java","-jar","/app/app.jar"]