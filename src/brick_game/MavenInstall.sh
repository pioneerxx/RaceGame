#!bin/bash

curl https://dlcdn.apache.org/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz --output maven.tar.gz
tar -xvf maven.tar.gz
rm maven.tar.gz
export PATH=apache-maven-3.9.6/bin:$PATH