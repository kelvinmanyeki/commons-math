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

package org.apache.commons.math4.benchmark;

import org.apache.commons.math4.core.jdkmath.AccurateMathCalcTestHelper;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Fork;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(2)
@State(Scope.Benchmark)
public class AccurateMathCalcBenchmark {

    @Param({"0.0", "0.5", "1.0", "2.5", "10.0", "100.0", "-1.5"})
    private double value;

    private double[] resultHolder;

    @Setup
    public void setup() {
        resultHolder = new double[2];
    }

    @Benchmark
    public double slowSin() {
        return AccurateMathCalcTestHelper.testSlowSin(value, resultHolder);
    }

    @Benchmark
    public double slowCos() {
        return AccurateMathCalcTestHelper.testSlowCos(value, resultHolder);
    }

    @Benchmark
    public double slowexp() {
        return AccurateMathCalcTestHelper.testSlowexp(value, resultHolder);
    }

    @Benchmark
    public double[] slowLog() {
        return AccurateMathCalcTestHelper.testSlowLog(value);
    }

    @Benchmark
    public double expint() {
        return AccurateMathCalcTestHelper.testExpint((int)value, resultHolder);
    }
}

