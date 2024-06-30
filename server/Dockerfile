FROM gradle:8.4.0-jdk8
LABEL authors="vasilyev"



ADD /target/server-1.0-SNAPSHOT.jar server



ENTRYPOINT ["java", "-jar", "/server"]