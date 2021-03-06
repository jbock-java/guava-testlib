/*
 * Copyright (C) 2015 The Guava Authors
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

import com.google.common.collect.testing.AbstractMapTester;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import java.util.Map;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.ALLOWS_NULL_KEYS;
import static com.google.common.collect.testing.features.MapFeature.ALLOWS_NULL_VALUES;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_PUT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * A generic JUnit test which tests {@link Map#computeIfAbsent}. Can't be invoked directly; please
 * see {@link com.google.common.collect.testing.MapTestSuiteBuilder}.
 *
 * @author Louis Wasserman
 */
public class MapComputeIfAbsentTester<K, V> extends AbstractMapTester<K, V> {

    @MapFeature.Require(SUPPORTS_PUT)
    public void testComputeIfAbsent_supportedAbsent() {
        assertEquals(
                v3(),
                getMap()
                        .computeIfAbsent(
                                k3(),
                                k -> {
                                    assertEquals(k3(), k);
                                    return v3();
                                }),
                "computeIfAbsent(notPresent, function) should return new value");
        expectAdded(e3());
    }

    @MapFeature.Require(SUPPORTS_PUT)
    @CollectionSize.Require(absent = ZERO)
    public void testComputeIfAbsent_supportedPresent() {
        assertEquals(
                v0(),
                getMap()
                        .computeIfAbsent(
                                k0(),
                                k -> {
                                    throw new AssertionError();
                                }),
                "computeIfAbsent(present, function) should return existing value");
        expectUnchanged();
    }

    @MapFeature.Require(SUPPORTS_PUT)
    public void testComputeIfAbsent_functionReturnsNullNotInserted() {
        assertNull(
                getMap()
                        .computeIfAbsent(
                                k3(),
                                k -> {
                                    assertEquals(k3(), k);
                                    return null;
                                }),
                "computeIfAbsent(absent, returnsNull) should return null");
        expectUnchanged();
    }

    @MapFeature.Require({SUPPORTS_PUT, ALLOWS_NULL_VALUES})
    @CollectionSize.Require(absent = ZERO)
    public void testComputeIfAbsent_nullTreatedAsAbsent() {
        initMapWithNullValue();
        assertEquals(
                getValueForNullKey(),
                getMap()
                        .computeIfAbsent(
                                getKeyForNullValue(),
                                k -> {
                                    assertEquals(getKeyForNullValue(), k);
                                    return getValueForNullKey();
                                }),
                "computeIfAbsent(presentAssignedToNull, function) should return newValue");
        expectReplacement(entry(getKeyForNullValue(), getValueForNullKey()));
    }

    @MapFeature.Require({SUPPORTS_PUT, ALLOWS_NULL_KEYS})
    public void testComputeIfAbsent_nullKeySupported() {
        getMap()
                .computeIfAbsent(
                        null,
                        k -> {
                            assertNull(k);
                            return v3();
                        });
        expectAdded(entry(null, v3()));
    }

    static class ExpectedException extends RuntimeException {
    }

    @MapFeature.Require(SUPPORTS_PUT)
    public void testComputeIfAbsent_functionThrows() {
        try {
            getMap()
                    .computeIfAbsent(
                            k3(),
                            k -> {
                                assertEquals(k3(), k);
                                throw new ExpectedException();
                            });
            fail("Expected ExpectedException");
        } catch (ExpectedException expected) {
        }
        expectUnchanged();
    }

    @MapFeature.Require(absent = SUPPORTS_PUT)
    public void testComputeIfAbsent_unsupportedAbsent() {
        try {
            getMap()
                    .computeIfAbsent(
                            k3(),
                            k -> {
                                // allowed to be called
                                assertEquals(k3(), k);
                                return v3();
                            });
            fail("computeIfAbsent(notPresent, function) should throw");
        } catch (UnsupportedOperationException expected) {
        }
        expectUnchanged();
    }

    @MapFeature.Require(absent = SUPPORTS_PUT)
    @CollectionSize.Require(absent = ZERO)
    public void testComputeIfAbsent_unsupportedPresentExistingValue() {
        try {
            assertEquals(
                    v0(),
                    getMap()
                            .computeIfAbsent(
                                    k0(),
                                    k -> {
                                        assertEquals(k0(), k);
                                        return v0();
                                    }),
                    "computeIfAbsent(present, returnsCurrentValue) should return present or throw");
        } catch (UnsupportedOperationException tolerated) {
        }
        expectUnchanged();
    }

    @MapFeature.Require(absent = SUPPORTS_PUT)
    @CollectionSize.Require(absent = ZERO)
    public void testComputeIfAbsent_unsupportedPresentDifferentValue() {
        try {
            assertEquals(
                    v0(),
                    getMap()
                            .computeIfAbsent(
                                    k0(),
                                    k -> {
                                        assertEquals(k0(), k);
                                        return v3();
                                    }),
                    "computeIfAbsent(present, returnsDifferentValue) should return present or throw");
        } catch (UnsupportedOperationException tolerated) {
        }
        expectUnchanged();
    }

    @MapFeature.Require(value = SUPPORTS_PUT, absent = ALLOWS_NULL_KEYS)
    public void testComputeIfAbsent_nullKeyUnsupported() {
        try {
            getMap()
                    .computeIfAbsent(
                            null,
                            k -> {
                                assertNull(k);
                                return v3();
                            });
            fail("computeIfAbsent(null, function) should throw");
        } catch (NullPointerException expected) {
        }
        expectUnchanged();
        expectNullKeyMissingWhenNullKeysUnsupported(
                "Should not contain null key after unsupported computeIfAbsent(null, function)");
    }
}
