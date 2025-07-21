#!/bin/bash
if [ -z "$JAVA_HOME" ]; then
  echo "Please set JAVA_HOME to your JDK path."
  exit 1
fi
./mvnw spring-boot:run 