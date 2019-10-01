FROM alpine/git as clone
ENV APPROOT="/app"
WORKDIR $APPROOT
RUN git clone https://github.com/navkkrnair/10-2019-booking-service.git

FROM maven:3.6-jdk-8-alpine as build
ENV APPROOT="/app"
WORKDIR $APPROOT
COPY --from=clone $APPROOT/10-2019-booking-service $APPROOT
RUN mvn package -DskipTests


FROM openjdk:8-jre-alpine
MAINTAINER "navkkrnair@gmail.com"
ENV APPROOT="/app"
WORKDIR $APPROOT 
COPY --from=build $APPROOT/target/booking-service-1.0.jar $APPROOT
EXPOSE 8060
ENTRYPOINT ["java"]
CMD ["-jar","-Xmx512m","-Xms512m","-Djava.security.egd=file:/dev/./urandom", "booking-service-1.0.jar"]
