FROM openjdk:11.0.2-jre-slim-stretch

# add jar to image
COPY ./target/product_management-service.jar product_management-service.jar

# Startup
CMD java ${JAVA_OPTS} -jar ./product_management-service.jar
