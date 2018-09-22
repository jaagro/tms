FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
ADD tms-web-2.0.0-SNAPSHOT.jar app.jar
RUN bash -c 'touch /app.jar'
RUN echo "Asia/shanghai" > /etc/timezone
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
EXPOSE 8010