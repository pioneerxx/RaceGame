#!bin/bash

mvn -f race/pom.xml install
mvn -f server/pom.xml package