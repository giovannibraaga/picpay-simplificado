FROM gradle:7.5.0-jdk17 AS build

WORKDIR /home/gradle/project

COPY . .

RUN ./gradlew shadowJar --no-daemon

FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY --from=build /home/gradle/project/build/libs/com.br.picpaysimplificado-all.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
