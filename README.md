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

## get all projects
	curl --header 'Authorization: Bearer <user_token>' http://localhost:9000/api/projects/search

## coverage:
	http://localhost:9000/api/measures/component?metricKeys=coverage&component=<project-key>
## bugs:
	http://localhost:9000/api/measures/component?metricKeys=bugs&component=<project-key>
## quality gate 
	http://localhost:9000/api/qualitygates/project_status?projectKey=<project-key>

#CLI commands to retrieve project details 
curl --location --request GET 'http://localhost:8088/geProjects' --header 'Authorization: Bearer <user_token>'

retrieve rules
http://sonarqube:9000/api/rules/search?activation=true&languages=java

## retrieve history records for history
http://sonarqube:9000/api/measures/search_history?metrics=<metrics>&component=gates-of-olympus

# Metrics - possible values are -
Bugs: bugs

Code Smells: code_smells

Coverage: coverage

Lines of Code: lines

Duplicated Lines: duplicated_lines

Complexity: complexity

New Code Coverage: new_coverage

Test Execution Time: test_execution_time

Test Failures: test_failures

Vulnerabilities: vulnerabilities

# example 
curl -x --header "Authorization: Bearer <user_token>" http://sonarqube:9000/api/measures/search_history?metrics=code_smells&component=gates-of-olympus

curl -x --header "Authorization: Bearer <user_token>" http://sonarqube:9000/api/issues/search?componentKeys=my_project_key&severities=CRITICAL,BLOCKER&types=BUG&resolved=false&ps=100

# get all typescript rules.
http://sonarqube:9000/api/rules/search?activation=true&languages=ts&ps=400