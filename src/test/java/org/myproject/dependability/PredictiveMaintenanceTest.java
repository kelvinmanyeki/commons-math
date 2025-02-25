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

package org.myproject.dependability;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

public class PredictiveMaintenanceTest {

    private PredictiveMaintenance pm;

    @Before
    public void setUp() {
        pm = new PredictiveMaintenance();
        pm.loadData(); // Load synthetic data before each test
    }

    @Test
    public void testPredictRUL() {
        // Test case 1: Known input from training data
        double temperature1 = 90.0;
        double expectedRUL1 = 80.0; // From training data
        double predictedRUL1 = pm.predictRUL(temperature1);
        assertEquals(expectedRUL1, predictedRUL1, 0.001);

        // Test case 2: New input (interpolation)
        double temperature2 = 92.0;
        double predictedRUL2 = pm.predictRUL(temperature2);
        assertTrue(predictedRUL2 > 70.0 && predictedRUL2 < 90.0); // Should be between 70 and 90 days

        // Test case 3: Extreme input (extrapolation)
        double temperature3 = 110.0;
        double predictedRUL3 = pm.predictRUL(temperature3);
        assertTrue(predictedRUL3 < 50.0); // Should be very low
    }

    @Test
    public void testModelTraining() {
        pm.trainModel(); // Ensure training works without errors
        assertNotNull(pm); // Just a sanity check
    }
}
