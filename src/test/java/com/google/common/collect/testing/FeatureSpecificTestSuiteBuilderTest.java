/*
 * Copyright (C) 2011 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.common.collect.testing;

import com.google.common.collect.testing.features.CollectionFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/** @author Max Ross */
class FeatureSpecificTestSuiteBuilderTest {

    private static boolean testWasRun;

    @BeforeEach
    void setUp() {
        testWasRun = false;
    }

    public static final class MyAbstractTester extends AbstractTester<Void> {
        public void testNothing() {
            testWasRun = true;
        }
    }

    private static final class MyTestSuiteBuilder
            extends FeatureSpecificTestSuiteBuilder<MyTestSuiteBuilder, String> {

        @Override
        protected List<Class<? extends AbstractTester>> getTesters() {
            return Collections.<Class<? extends AbstractTester>>singletonList(MyAbstractTester.class);
        }
    }

    @Test
    void testLifecycle() throws Throwable {
        boolean[] setUp = {false};
        Runnable setUpRunnable = () -> setUp[0] = true;
        boolean[] tearDown = {false};
        Runnable tearDownRunnable = () -> tearDown[0] = true;
        new MyAbstractTester().run();
        MyTestSuiteBuilder builder = new MyTestSuiteBuilder();
        builder.usingGenerator("yam")
                .named("yam")
                .withFeatures(CollectionFeature.NONE)
                .withSetUp(setUpRunnable)
                .withTearDown(tearDownRunnable)
                .createTestSuite()
                .run();
        assertTrue(testWasRun);
        assertTrue(setUp[0]);
        assertTrue(tearDown[0]);
    }
}
