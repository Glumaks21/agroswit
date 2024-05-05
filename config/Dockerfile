FROM eclipse-temurin:21 as jre-build

COPY target/*.jar /app/app.jar
WORKDIR /app

RUN jar xf app.jar
RUN jdeps \
    --ignore-missing-deps \
    --print-module-deps \
    --multi-release 21 \
    --recursive \
    --class-path 'BOOT-INF/lib/*' \
    app.jar > modules.txt

RUN $JAVA_HOME/bin/jlink \
         --add-modules $(cat modules.txt) \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=2 \
         --output /javaruntime

FROM debian:buster-slim
RUN apt-get -y update
RUN apt-get -y install curl

ENV JAVA_HOME=/opt/java/openjdk
ENV PATH "${JAVA_HOME}/bin:${PATH}"
COPY --from=jre-build /javaruntime $JAVA_HOME

RUN mkdir /opt/server
COPY --from=jre-build /app/app.jar /opt/server/
ENTRYPOINT ["java", "-jar", "/opt/server/app.jar"]