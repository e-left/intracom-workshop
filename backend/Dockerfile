FROM gradle:6.8.3-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:11.0.11-jre

EXPOSE 4567

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/*.jar /app/cell-log-filter.jar

ENTRYPOINT ["java","-jar","/app/cell-log-filter.jar"]