FROM openjdk:11

ADD target/account-service.jar /usr/local/account-service/

WORKDIR /usr/local/account-service/

CMD ["java", "-jar", "account-service.jar"]