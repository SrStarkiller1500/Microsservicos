#!/bin/bash
# Build both services and start with docker-compose
./gradlew :storefront-service:bootJar :warehouse-service:bootJar
docker-compose up --build -d
