FROM amazoncorretto:21-alpine
COPY target/*.jar app.jar
ENV SPRING_PROFILES_ACTIVE=docker
ENTRYPOINT ["java","-jar","/app.jar"]