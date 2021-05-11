FROM openjdk:11.0.11-jdk
COPY build/libs/keymanager-rest-0.1-all.jar /etc/keymanager-REST.jar
WORKDIR /etc
EXPOSE 8080
CMD ["java", "-jar", "keymanager-REST.jar"]