# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements. See the NOTICE file for details.
# Licensed under the Apache License, Version 2.0.

# Build stage
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copy build configuration files first to leverage Docker cache
COPY pom.xml .
COPY libs/randoop-4.3.3.jar libs/

# Install Randoop dependency
RUN mvn install:install-file \
    -Dfile=libs/randoop-4.3.3.jar \
    -DgroupId=org.randoop \
    -DartifactId=randoop \
    -Dversion=4.3.3 \
    -Dpackaging=jar \
    -DgeneratePom=true

# Download dependencies (this will be cached unless pom.xml changes)
RUN mvn dependency:go-offline

# Copy source files
COPY src/ ./src/

# Build application
RUN mvn -V --no-transfer-progress clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy the built jar from the build stage
# Note: Using a specific path pattern is safer than wildcard
COPY --from=build /app/predictive-maintenance/target/predictive-maintenance-*.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# Run the application with recommended JVM options
ENTRYPOINT ["java", "-jar", "app.jar"]
