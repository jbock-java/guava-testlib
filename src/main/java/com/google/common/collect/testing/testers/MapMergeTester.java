/*
 * Copyright (C) 2016 The Guava Authors
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

package com.google.common.collect.testing.testers;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.collect.testing.AbstractMapTester;
import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import java.lang.reflect.Method;
import java.util.Map;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.ALLOWS_NULL_KEYS;
import static com.google.common.collect.testing.features.MapFeature.ALLOWS_NULL_VALUES;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_PUT;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_REMOVE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * A generic JUnit test which tests {@link Map#merge}. Can't be invoked directly; please see {@link
 * com.google.common.collect.testing.MapTestSuiteBuilder}.
 *
 * @author Louis Wasserman
 */
public class MapMergeTester<K, V> extends AbstractMapTester<K, V> {
    @MapFeature.Require(SUPPORTS_PUT)
    public void testAbsent() {
        assertEquals(
                v3(),
                getMap()
                        .merge(
                                k3(),
                                v3(),
                                (oldV, newV) -> {
                                    throw new AssertionError(
                                            "Should not call merge function if key was absent");
                                }),
                "Map.merge(absent, value, function) should return value");
        expectAdded(e3());
    }

    @MapFeature.Require({SUPPORTS_PUT, ALLOWS_NULL_VALUES})
    @CollectionSize.Require(absent = ZERO)
    public void testMappedToNull() {
        initMapWithNullValue();
        assertEquals(
                v3(),
                getMap()
                        .merge(
                                getKeyForNullValue(),
                                v3(),
                                (oldV, newV) -> {
                                    throw new AssertionError(
                                            "Should not call merge function if key was mapped to null");
                                }),
                "Map.merge(keyMappedToNull, value, function) should return value");
        expectReplacement(entry(getKeyForNullValue(), v3()));
    }

    @MapFeature.Require({SUPPORTS_PUT, ALLOWS_NULL_KEYS})
    public void testMergeAbsentNullKey() {
        assertEquals(
                v3(),
                getMap()
                        .merge(
                                null,
                                v3(),
                                (oldV, newV) -> {
                                    throw new AssertionError(
                                            "Should not call merge function if key was absent");
                                }),
                "Map.merge(null, value, function) should return value");
        expectAdded(entry(null, v3()));
    }

    @MapFeature.Require(SUPPORTS_PUT)
    @CollectionSize.Require(absent = ZERO)
    public void testMergePresent() {
        assertEquals(
                v4(),
                getMap()
                        .merge(
                                k0(),
                                v3(),
                                (oldV, newV) -> {
                                    assertEquals(v0(), oldV);
                                    assertEquals(v3(), newV);
                                    return v4();
                                }),
                "Map.merge(present, value, function) should return function result");
        expectReplacement(entry(k0(), v4()));
    }

    private static class ExpectedException extends RuntimeException {
    }

    @MapFeature.Require(SUPPORTS_PUT)
    @CollectionSize.Require(absent = ZERO)
    public void testMergeFunctionThrows() {
        try {
            getMap()
                    .merge(
                            k0(),
                            v3(),
                            (oldV, newV) -> {
                                assertEquals(v0(), oldV);
                                assertEquals(v3(), newV);
                                throw new ExpectedException();
                            });
            fail("Expected ExpectedException");
        } catch (ExpectedException expected) {
        }
        expectUnchanged();
    }

    @MapFeature.Require(SUPPORTS_REMOVE)
    @CollectionSize.Require(absent = ZERO)
    public void testMergePresentToNull() {
        assertNull(
                getMap()
                        .merge(
                                k0(),
                                v3(),
                                (oldV, newV) -> {
                                    assertEquals(v0(), oldV);
                                    assertEquals(v3(), newV);
                                    return null;
                                }),
                "Map.merge(present, value, functionReturningNull) should return null");
        expectMissing(e0());
    }

    public void testMergeNullValue() {
        try {
            getMap()
                    .merge(
                            k0(),
                            null,
                            (oldV, newV) -> {
                                throw new AssertionError("Should not call merge function if value was null");
                            });
            fail("Expected NullPointerException or UnsupportedOperationException");
        } catch (NullPointerException | UnsupportedOperationException expected) {
        }
    }

    public void testMergeNullFunction() {
        try {
            getMap().merge(k0(), v3(), null);
            fail("Expected NullPointerException or UnsupportedOperationException");
        } catch (NullPointerException | UnsupportedOperationException expected) {
        }
    }

    @MapFeature.Require(absent = SUPPORTS_PUT)
    public void testMergeUnsupported() {
        try {
            getMap()
                    .merge(
                            k3(),
                            v3(),
                            (oldV, newV) -> {
                                throw new AssertionError();
                            });
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
        }
    }

    /**
     * Returns the {@link Method} instance for {@link #testMergeNullValue()} so that tests of {@code
     * Hashtable} can suppress it with {@code FeatureSpecificTestSuiteBuilder.suppressing()}.
     */
    @GwtIncompatible // reflection
    public static Method getMergeNullValueMethod() {
        return Helpers.getMethod(MapMergeTester.class, "testMergeNullValue");
    }
}
