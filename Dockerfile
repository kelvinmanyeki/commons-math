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

# Copy build essentials first
COPY pom.xml .
COPY libs/randoop-4.3.3.jar libs/

# Install Randoop early to leverage caching
RUN mvn install:install-file \
    -Dfile=libs/randoop-4.3.3.jar \
    -DgroupId=org.randoop \
    -DartifactId=randoop \
    -Dversion=4.3.3 \
    -Dpackaging=jar \
    -DgeneratePom=true

# Copy remaining source files
COPY src/ ./src/

# Build application
RUN mvn -V --no-transfer-progress package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/predictive-maintenance/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
