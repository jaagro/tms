#基础镜像也可以用 java:8
FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD tms-web-2.0.0-SNAPSHOT.jar app.jar
RUN echo "Asia/shanghai" > /etc/timezone
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
EXPOSE 8010