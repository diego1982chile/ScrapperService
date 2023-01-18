FROM openjdk:8-jdk-alpine
ADD payara-micro-5.2020.3.jar payara-micro-5.2020.3.jar
ADD target/ScrapperConfig.war ScrapperConfig.war
ENTRYPOINT ["sh", "-c", "java -jar payara-micro-5.2020.3.jar --deploy ScrapperConfig.war"]