FROM openjdk:11.0.2-jre-slim-stretch

# add jar to image
COPY ./target/order_management-service.jar order_management-service.jar

# Startup
CMD java ${JAVA_OPTS} -jar ./order_management-service.jar
