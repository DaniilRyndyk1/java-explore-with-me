FROM amazoncorretto:11-alpine-jdk
COPY /service/target/*.jar service.jar
ENTRYPOINT ["java","-jar","/service.jar"]