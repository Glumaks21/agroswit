FROM amazoncorretto:21
COPY target/*.jar app.jar
ENV SPRING_PROFILES_ACTIVE=docker
ENTRYPOINT ["java","-jar","app.jar"]