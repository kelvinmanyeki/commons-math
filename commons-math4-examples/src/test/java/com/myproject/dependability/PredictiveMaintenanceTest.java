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
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PredictiveMaintenanceTest {

    @Test
    void testPredictRUL() {
        SimpleRegression regression = new SimpleRegression();
        
        // Load test data
        regression.addData(80, 50);
        regression.addData(85, 45);
        regression.addData(90, 40);
        regression.addData(95, 35);
        regression.addData(100, 30);

        // Expected RUL for a temperature of 92 (based on our linear model)
        double expectedRUL = regression.predict(92);

        // Check if prediction matches the expected value
        assertEquals(38.0, expectedRUL, 0.1);  // Allowing a small margin of error
    }

    @Test
    void testRegressionModelTraining() {
        SimpleRegression regression = new SimpleRegression();
        regression.addData(80, 50);
        regression.addData(100, 30);

        // Expected slope calculation: (y2 - y1) / (x2 - x1) = (30 - 50) / (100 - 80) = -1
        assertEquals(-1.0, regression.getSlope(), 0.01);

        // Expected intercept calculation: y = mx + b --> b = y - mx = 50 - (-1 * 80) = 130
        assertEquals(130.0, regression.getIntercept(), 0.01);
    }
}
