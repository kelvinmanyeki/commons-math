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

# Stage 1: Build stage
FROM maven:3.8.6-eclipse-temurin-17 as builder

WORKDIR /app

# Copy parent POM and install
COPY pom.xml .
RUN mvn -N install -Dcheckstyle.skip=true -Drat.skip=true

# Copy module files
COPY predictive-maintenance/pom.xml predictive-maintenance/
COPY predictive-maintenance/src/ predictive-maintenance/src/

# Build the fat JAR
RUN mvn -f predictive-maintenance/pom.xml clean package

# Stage 2: Runtime image
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/predictive-maintenance/target/predictive-maintenance-*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
