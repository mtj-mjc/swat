FROM openjdk:11.0.2-jre-slim-stretch

# add jar to image
COPY ./target/user_management-service.jar user_management-service.jar

# Startup
CMD java ${JAVA_OPTS} -jar ./user_management-service.jar
