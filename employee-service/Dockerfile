FROM openjdk:17-jdk
VOLUME /tmp
COPY build/libs/employee-service-0.0.1-SNAPSHOT.jar employee-service.jar
ENTRYPOINT ["java","-jar","/employee-service.jar"]
