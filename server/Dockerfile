FROM gradle:8.4.0-jdk8
LABEL authors="vasilyev"

COPY /build/libs/server-1.0-SNAPSHOT.jar /tic-tac-toe.jar
ENTRYPOINT ["java", "-jar", "/tic-tac-toe.jar"]