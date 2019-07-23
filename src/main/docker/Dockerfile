FROM openjdk:11

ADD account-service.jar /usr/local/account-service/

WORKDIR /usr/local/account-service/

CMD ["java", "-Xms512m", "-Xmx1g", "-jar", "account-service.jar"]