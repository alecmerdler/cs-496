#/bin/bash

echo "Starting server"
mvn clean install -DskipTests=true
mvn ninja:run

curl -X POST 127.0.0.1:8080/initialize
