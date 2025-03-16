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
 import static org.junit.jupiter.api.Assertions.*;
 import java.io.ByteArrayOutputStream;
 import java.io.PrintStream;
 import java.lang.reflect.Constructor;
 
 class MainTest {
 
     @Test
     void testCalculateBaseFailureProbability() {
         double[] features = {100, 30, 15};
         double expected = 0.1225;
         double actual = Main.calculateBaseFailureProbability(features);
         assertEquals(expected, actual, 0.0001);
     }
 
     @Test
     void testCalculatePrediction() {
         double[] params = {0.1, 0.0005, 0.002, 0.001};
         double[] input = {200, 35, 20};
         double expected = 0.29;
         double actual = Main.calculatePrediction(params, input);
         assertEquals(expected, actual, 0.0001);
     }
 
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
 
     @Test
     void testMainMethod() {
         ByteArrayOutputStream outContent = new ByteArrayOutputStream();
         System.setOut(new PrintStream(outContent));
         Main.main(new String[]{});
         String logOutput = outContent.toString();
         assertTrue(logOutput.contains("Predicted failure probability for 1100h/55°C/35mm:"));
     }
 
     @Test
     void testCalculateBaseFailureProbability_InvalidInput() {
         assertThrows(IllegalArgumentException.class, () -> {
             Main.calculateBaseFailureProbability(new double[]{100, 30});
         });
     }
 
     @Test
     void testCalculatePrediction_InvalidParameters() {
         assertThrows(IllegalArgumentException.class, () -> {
             Main.calculatePrediction(new double[]{0.1, 0.0005}, new double[]{200, 35, 20});
         });
     }
 
     @Test
     void testConstructor() throws Exception {
         Constructor<Main> constructor = Main.class.getDeclaredConstructor();
         assertFalse(constructor.isAccessible());
         constructor.setAccessible(true);
         assertThrows(AssertionError.class, constructor::newInstance);
     }
 }