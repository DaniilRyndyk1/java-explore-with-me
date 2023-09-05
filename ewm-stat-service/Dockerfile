FROM amazoncorretto:19-alpine-jdk
COPY /service/target/*.jar service.jar
ENTRYPOINT ["java","-jar","/service.jar"]