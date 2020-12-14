FROM tomcat:10.0-jdk8-openjdk-slim
COPY repo/target/dependency-jars /run/dependency-jars
ADD repo/target/application.jar /run/application.jar

CMD ls /run

ENTRYPOINT java -jar /run/application.jar -D exec.mainClass="com.lordkada.telr.TelrApplication"