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

 package com.myproject;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Predictive maintenance model using three-variable linear regression.
 */
public final class Main {

    /**
     * Logger instance for logging messages.
     */
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    private Main() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Main method demonstrating predictive maintenance model.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(final String[] args) {
        final OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();

        // Three-variable input: [operating_hours, temperature, vibration]
        final double[][] x = {
                {100, 30, 15}, {200, 32, 16}, {300, 35, 18},
                {400, 37, 20}, {500, 40, 22}, {600, 42, 24},
                {700, 45, 26}, {800, 47, 28}, {900, 50, 30}, {1000, 53, 32}
        };

        // Generate y-values without random noise
        final double[] y = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            y[i] = calculateBaseFailureProbability(x[i]);
        }

        regression.newSampleData(y, x);

        final double[] params = regression.estimateRegressionParameters();
        final double[] input = {1100, 55, 35}; // Three-variable input
        final double prediction = calculatePrediction(params, input);

        LOG.info("Predicted failure probability for 1100h/55°C/35mm: {}", prediction);
    }

    /**
     * Calculates failure probability prediction using regression parameters.
     *
     * @param params regression parameters [intercept, hourCoef, tempCoef, vibCoef]
     * @param input  input values [hours, temp, vibration]
     * @return predicted failure probability
     * @throws IllegalArgumentException if params or input is invalid
     */
    public static double calculatePrediction(final double[] params, final double[] input) {
        if (params == null || params.length != 4) {
            throw new IllegalArgumentException("Params array must have exactly 4 elements.");
        }
        if (input == null || input.length != 3) {
            throw new IllegalArgumentException("Input array must have exactly 3 elements.");
        }
        return params[0] + params[1] * input[0] + params[2] * input[1] + params[3] * input[2];
    }

    /**
     * Base failure probability calculation (example implementation).
     *
     * @param features input features [hours, temp, vibration]
     * @return base failure probability without noise
     * @throws IllegalArgumentException if features is invalid
     */
    public static double calculateBaseFailureProbability(final double[] features) {
        if (features == null || features.length != 3) {
            throw new IllegalArgumentException("Input array must have exactly 3 elements.");
        }
        return (features[0] * 0.0004) + (features[1] * 0.002) + (features[2] * 0.0015);
    }
}