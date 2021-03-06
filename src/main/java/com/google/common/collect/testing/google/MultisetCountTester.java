/*
 * Copyright (C) 2013 The Guava Authors
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

package com.google.common.collect.testing.google;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.WrongType;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static com.google.common.collect.testing.features.CollectionFeature.ALLOWS_NULL_QUERIES;
import static com.google.common.collect.testing.features.CollectionFeature.ALLOWS_NULL_VALUES;
import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests for {@code Multiset#count}.
 *
 * @author Jared Levy
 */
@GwtCompatible(emulated = true)
public class MultisetCountTester<E> extends AbstractMultisetTester<E> {

    public void testCount_0() {
        assertEquals(
                0,
                getMultiset().count(e3()),
                "multiset.count(missing) didn't return 0");
    }

    @CollectionSize.Require(absent = ZERO)
    public void testCount_1() {
        assertEquals(
                1,
                getMultiset().count(e0()),
                "multiset.count(present) didn't return 1");
    }

    @CollectionSize.Require(SEVERAL)
    public void testCount_3() {
        initThreeCopies();
        assertEquals(
                3,
                getMultiset().count(e0()),
                "multiset.count(thriceContained) didn't return 3");
    }

    @CollectionFeature.Require(ALLOWS_NULL_QUERIES)
    public void testCount_nullAbsent() {
        assertEquals(
                0,
                getMultiset().count(null),
                "multiset.count(null) didn't return 0");
    }

    @CollectionFeature.Require(absent = ALLOWS_NULL_QUERIES)
    public void testCount_null_forbidden() {
        try {
            getMultiset().count(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException expected) {
        }
    }

    @CollectionSize.Require(absent = ZERO)
    @CollectionFeature.Require(ALLOWS_NULL_VALUES)
    public void testCount_nullPresent() {
        initCollectionWithNullElement();
        assertEquals(1, getMultiset().count(null));
    }

    public void testCount_wrongType() {
        assertEquals(
                0,
                getMultiset().count(WrongType.VALUE),
                "multiset.count(wrongType) didn't return 0");
    }

    /**
     * Returns {@link Method} instances for the read tests that assume multisets support duplicates so
     * that the test of {@code Multisets.forSet()} can suppress them.
     */
    @GwtIncompatible // reflection
    public static List<Method> getCountDuplicateInitializingMethods() {
        return Arrays.asList(Helpers.getMethod(MultisetCountTester.class, "testCount_3"));
    }
}
