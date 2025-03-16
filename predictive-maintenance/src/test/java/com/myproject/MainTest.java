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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

/**
 * Unit tests for Main class methods.
 */
class MainTest {

    /**
     * Tests base failure probability calculation.
     */
    @Test
    void testCalculateBaseFailureProbability() {
        double[] features = {100, 30, 15};
        double expected = 0.1225;
        double actual = Main.calculateBaseFailureProbability(features);
        assertEquals(expected, actual, 0.0001);
    }

    /**
     * Tests failure probability prediction calculation.
     */
    @Test
    void testCalculatePrediction() {
        double[] params = {0.1, 0.0005, 0.002, 0.001};
        double[] input = {200, 35, 20};
        double expected = 0.29;
        double actual = Main.calculatePrediction(params, input);
        assertEquals(expected, actual, 0.0001);
    }

    /**
     * Tests regression model sample data and parameter estimation.
     */
    @Test
    void testRegressionModel() {
        OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
        double[][] x = {
                {100, 30, 15}, {200, 32, 16}, {300, 35, 18},
                {400, 37, 20}, {500, 40, 22}, {600, 42, 24},
                {700, 45, 26}, {800, 47, 28}, {900, 50, 30}, {1000, 53, 32}
        };
        double[] y = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            y[i] = Main.calculateBaseFailureProbability(x[i]);
        }
        regression.newSampleData(y, x);
        double[] params = regression.estimateRegressionParameters();
        assertNotNull(params);
        assertEquals(4, params.length);
        double[] input = {1100, 55, 35};
        double prediction = Main.calculatePrediction(params, input);
        assertTrue(prediction >= 0 && prediction <= 1);
    }

    /**
     * Tests main method execution and log output capture.
     */
    @Test
    void testMainMethod() {
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(errContent));
        Main.main(new String[]{});
        System.setErr(originalErr);
        String logOutput = errContent.toString();
        assertTrue(logOutput.contains("Predicted failure probability for 1100h/55°C/35mm:"),
                "Log output should contain: " + logOutput);
    }

    /**
     * Tests invalid input handling for base failure probability calculation.
     */
    @Test
    void testCalculateBaseFailureProbability_InvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            Main.calculateBaseFailureProbability(new double[]{100, 30});
        });
    }

    /**
     * Tests invalid parameter handling for prediction calculation.
     */
    @Test
    void testCalculatePrediction_InvalidParameters() {
        assertThrows(IllegalArgumentException.class, () -> {
            Main.calculatePrediction(new double[]{0.1, 0.0005}, new double[]{200, 35, 20});
        });
    }

    /**
     * Tests private constructor to ensure instantiation is not allowed.
     */
    @Test
    void testConstructor() throws Exception {
        Constructor<Main> constructor = Main.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()), "Constructor should be private");
        constructor.setAccessible(true);
        Exception exception = assertThrows(InvocationTargetException.class, constructor::newInstance);
        assertTrue(exception.getCause() instanceof UnsupportedOperationException,
                "Cause should be UnsupportedOperationException");
    }
}
