FROM tomcat:10.0-jdk8-openjdk-slim

COPY target/dependency-jars /run/dependency-jars
ADD target/application.jar /run/application.jar

ENTRYPOINT java -jar /run/application.jar -D exec.mainClass="com.lordkada.telr.TelrApplication"