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
	http://localhost:9000/api/measures/component?metricKeys=coverage&component=casino-trader
## bugs:
	http://localhost:9000/api/measures/component?metricKeys=bugs&component=casino-trader
## quality gate 
	http://localhost:9000/api/qualitygates/project_status?projectKey=casino-trader



