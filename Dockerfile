FROM  amazoncorretto:21-alpine

COPY target/agroswit-1.0.0.jar /agroswit.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "agroswit.jar"]