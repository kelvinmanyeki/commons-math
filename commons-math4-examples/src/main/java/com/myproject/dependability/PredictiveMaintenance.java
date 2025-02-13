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


package com.myproject.dependability;

import org.apache.commons.math4.stat.regression.SimpleRegression;

public class PredictiveMaintenance {
    public static void main(String[] args) {
        // Step 1: Create a regression model
        SimpleRegression regression = new SimpleRegression();

        // Step 2: Load synthetic data (Temperature vs. Remaining Useful Life)
        loadData(regression);

        // Step 3: Train the model and print the slope and intercept
        trainModel(regression);

        // Step 4: Predict Remaining Useful Life (RUL) for a static temperature value
        double newTemperature = 92.0;  // Static input for temperature
        double predictedRUL = predictRUL(regression, newTemperature);
        
        // Display the results
        System.out.println("Predicted RUL for Temperature " + newTemperature + ": " + predictedRUL);
    }

    // Load synthetic data into the regression model
    private static void loadData(SimpleRegression regression) {
        regression.addData(80, 50);
        regression.addData(85, 45);
        regression.addData(90, 40);
        regression.addData(95, 35);
        regression.addData(100, 30);
        regression.addData(105, 25);
        regression.addData(110, 20);
        regression.addData(115, 15);
    }

    // Train the regression model and display training info
    private static void trainModel(SimpleRegression regression) {
        System.out.println("Training model...");
        System.out.println("Slope: " + regression.getSlope());
        System.out.println("Intercept: " + regression.getIntercept());
    }

    // Predict the Remaining Useful Life (RUL) for a given temperature
    private static double predictRUL(SimpleRegression regression, double temperature) {
        return regression.predict(temperature);
    }
}
