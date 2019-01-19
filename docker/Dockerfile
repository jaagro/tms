#基础镜像也可以用 java:8
FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD tms-web-2.0.0-SNAPSHOT.jar app.jar
RUN echo "Asia/shanghai" > /etc/timezone
ENTRYPOINT ["java","-Xmx1024m -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
EXPOSE 8010
EXPOSE 5005