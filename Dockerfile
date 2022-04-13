#using alpine as the base image
FROM openjdk:8-alpine

LABEL Description="This image is used to start laminar web service" Vendor="platformservices" Version="1.0"

RUN apk update && apk add tzdata && apk add mysql-client
RUN cp /usr/share/zoneinfo/Asia/Kolkata  /etc/localtime
RUN echo "Asia/Kolkata" >  /etc/timezone

RUN mkdir -p /laminar-web

ADD ./target/laminar-web-0.0.1-SNAPSHOT.jar laminar-web.jar
COPY jdbc.conf etc/

EXPOSE 8080
EXPOSE 8000

CMD ["java", "-jar", "laminar-web.jar"]
