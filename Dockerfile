#base docker image
FROM openjdk:17
LABEL maintainer ="sonar-dashboard"
ADD target/sonar-dashboard-1.0.jar sonar-dashboard.jar
ENTRYPOINT ["java","-jar","sonar-dashboard.jar"]
