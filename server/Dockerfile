FROM gradle:8.4.0-jdk8
LABEL authors="vasilyev"

WORKDIR /

COPY / .

RUN gradle build

ENTRYPOINT ["java", "-jar", "/build/libs/server-1.0-SNAPSHOT.jar"]