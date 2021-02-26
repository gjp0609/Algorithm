FROM openjdk:11-jre-slim-buster
VOLUME /Files
ADD ./target/spring.jar app.jar
ENTRYPOINT ["java", "-jar", "/data/app.jar"]