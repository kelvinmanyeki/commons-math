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
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Unit tests for Main class methods.
 */
class MainTest {

    /**
     * Tests the calculateBaseFailureProbability method.
     */
    @Test
    void testCalculateBaseFailureProbability() {
        // Test input: [operating_hours, temperature, vibration]
        double[] features = {100, 30, 15};

        // Expected result: (100 * 0.0004) + (30 * 0.002) + (15 * 0.0015) = 0.1225
        double expected = 0.1225;
        double actual = Main.calculateBaseFailureProbability(features);

        assertEquals(expected, actual, 0.0001, "Base failure probability calculation is incorrect");
    }

    /**
     * Tests the calculatePrediction method.
     */
    @Test
    void testCalculatePrediction() {
        // Regression parameters: [intercept, hourCoef, tempCoef, vibCoef]
        double[] params = {0.1, 0.0005, 0.002, 0.001};

        // Test input: [hours, temp, vibration]
        double[] input = {200, 35, 20};

        // Expected result: 0.1 + (200 * 0.0005) + (35 * 0.002) + (20 * 0.001) = 0.29
        double expected = 0.29;
        double actual = Main.calculatePrediction(params, input);

        assertEquals(expected, actual, 0.0001, "Prediction calculation is incorrect");
    }

    /**
     * Tests the regression model using OLSMultipleLinearRegression.
     */
    @Test
    void testRegressionModel() {
        // Create a regression instance
        OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();

        // Sample input data: [operating_hours, temperature, vibration]
        double[][] x = {
                {100, 30, 15}, {200, 32, 16}, {300, 35, 18},
                {400, 37, 20}, {500, 40, 22}, {600, 42, 24},
                {700, 45, 26}, {800, 47, 28}, {900, 50, 30}, {1000, 53, 32}
        };

        // Generate y-values using the base failure probability calculation
        double[] y = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            y[i] = Main.calculateBaseFailureProbability(x[i]);
        }

        // Add sample data to the regression model
        regression.newSampleData(y, x);

        // Estimate regression parameters
        double[] params = regression.estimateRegressionParameters();

        // Verify that the regression parameters are not null
        assertNotNull(params, "Regression parameters should not be null");

        // Verify that the number of parameters is correct (intercept + 3 coefficients)
        assertEquals(4, params.length, "Number of regression parameters is incorrect");

        // Test prediction with new input
        double[] input = {1100, 55, 35};
        double prediction = Main.calculatePrediction(params, input);

        // Verify that the prediction is a valid probability (between 0 and 1)
        assertTrue(prediction >= 0 && prediction <= 1, "Prediction should be a valid probability");
    }
}
