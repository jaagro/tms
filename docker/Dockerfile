FROM java:8
VOLUME /tmp
ADD tms-web-0.0.1-SNAPSHOT.jar app.jar
RUN bash -c 'touch /app.jar'
RUN echo "Asia/shanghai" > /etc/timezone
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
EXPOSE 8010