/*
 * Copyright (C) 2009 The Guava Authors
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

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import static com.google.common.collect.testing.testers.CollectionAddAllTester.getAddAllNullUnsupportedMethod;
import static com.google.common.collect.testing.testers.CollectionAddTester.getAddNullSupportedMethod;
import static com.google.common.collect.testing.testers.CollectionAddTester.getAddNullUnsupportedMethod;
import static com.google.common.collect.testing.testers.CollectionCreationTester.getCreateWithNullUnsupportedMethod;
import static com.google.common.collect.testing.testers.SetAddTester.getAddSupportedNullPresentMethod;

/**
 * Tests the {@link Set} implementations of {@link java.util}, suppressing tests that trip known
 * OpenJDK 6 bugs.
 *
 * @author Kevin Bourrillion
 */
class OpenJdk6SetTests {

    @Test
    void test() throws Throwable {
        new TestsForSetsInJavaUtil(){
            @Override
            protected Collection<Method> suppressForTreeSetNatural() {
                return Arrays.asList(
                        getAddNullUnsupportedMethod(),
                        getAddAllNullUnsupportedMethod(),
                        getCreateWithNullUnsupportedMethod());
            }

            @Override
            protected Collection<Method> suppressForCheckedSet() {
                return Arrays.asList(getAddNullSupportedMethod(), getAddSupportedNullPresentMethod());
            }

        }.allTests().run();
    }
}
