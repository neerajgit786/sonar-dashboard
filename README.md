# Welcome to custom sonar dashboard application

## 1. How to start
```bash
$ git clone (https://github.com/neerajgit786/sonar-dashboard.git)

$ cd sonar-dashboard

# Tomcat started at 8080
$ mvn spring-boot:run

# test
curl localhost:8080

```
##few sonar api sample request

## coverage:
	http://localhost:9000/api/measures/component?metricKeys=coverage&component=<project-key>
## bugs:
	http://localhost:9000/api/measures/component?metricKeys=bugs&component=<project-key>
## quality gate 
	http://localhost:9000/api/qualitygates/project_status?projectKey=<project-key>

#CLI commands to retrieve project details 
curl --location --request GET 'http://localhost:8088/geProjects' --header 'Authorization: Bearer <user_token>'

