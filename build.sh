#!/bin/bash

mvn clean \
  com.savage7.maven.plugins:maven-external-dependency-plugin:resolve-externa \
  com.savage7.maven.plugins:maven-external-dependency-plugin:install-external

mvn clean package "$@"

