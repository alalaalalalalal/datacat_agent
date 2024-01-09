#FROM openjdk:17-alpine
#VOLUME /tmp
#ADD target/datacat_agent-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
# bastion host에서 shell script 실행 해야 하므로 도커라이징 불가함
