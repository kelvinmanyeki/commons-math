/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.math4.dependability;

import org.apache.commons.math3.stat.regression.SimpleRegression;
import java.util.logging.Logger;

/**
 * PredictiveMaintenance simulates a predictive maintenance system for a machine.
 * It uses temperature data to predict the Remaining Useful Life (RUL).
 */
public class PredictiveMaintenance {

    /**
     * Logger instance for logging messages.
     */
    private static final Logger LOGGER = Logger.getLogger(PredictiveMaintenance.class.getName());

    /**
     * Regression model used for predicting Remaining Useful Life (RUL).
     */
    private SimpleRegression regression;

    public PredictiveMaintenance() {
        regression = new SimpleRegression();
    }

    /**
     * Loads synthetic sensor data into the regression model.
     */
    public void loadData() {
        // Synthetic data: [Temperature (°C), RUL (days)]
        regression.addData(80.0, 100.0); // Low temperature -> high RUL
        regression.addData(85.0, 90.0);
        regression.addData(90.0, 80.0);
        regression.addData(95.0, 70.0);
        regression.addData(100.0, 60.0); // High temperature -> low RUL
    }

    /**
     * Trains the regression model and prints the slope and intercept.
     */
    public void trainModel() {
        LOGGER.info("Training predictive maintenance model...");
        LOGGER.info("Slope: " + regression.getSlope());
        LOGGER.info("Intercept: " + regression.getIntercept());
    }

    /**
     * Predicts the Remaining Useful Life (RUL) based on temperature.
     *
     * @param temperature The temperature reading (in °C).
     * @return The predicted RUL (in days).
     */
    public double predictRUL(double temperature) {
        return regression.predict(temperature);
    }

    public static void main(String[] args) {
        PredictiveMaintenance pm = new PredictiveMaintenance();
        pm.loadData();
        pm.trainModel();
        double predictedRUL = pm.predictRUL(92.0);
        LOGGER.info("Predicted RUL for Temperature 92.0°C: " + predictedRUL + " days");
    }
}
