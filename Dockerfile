FROM ubuntu:latest

WORKDIR /app

RUN apt-get update && \
    apt-get install -y openjdk-17-jdk maven

COPY . /app /app

RUN mvn install

CMD mvn spring-boot:run