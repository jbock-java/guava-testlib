/*
 * Copyright (C) 2008 The Guava Authors
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
import com.google.common.collect.testing.WrongType;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.ALLOWS_NULL_KEYS;
import static com.google.common.collect.testing.features.MapFeature.ALLOWS_NULL_KEY_QUERIES;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A generic JUnit test which tests {@code containsKey()} operations on a map. Can't be invoked
 * directly; please see {@link com.google.common.collect.testing.MapTestSuiteBuilder}.
 *
 * @author George van den Driessche
 */
public class MapContainsKeyTester<K, V> extends AbstractMapTester<K, V> {
    @CollectionSize.Require(absent = ZERO)
    public void testContains_yes() {
        assertTrue(
                getMap().containsKey(k0()),
                "containsKey(present) should return true");
    }

    public void testContains_no() {
        assertFalse(
                getMap().containsKey(k3()),
                "containsKey(notPresent) should return false");
    }

    @MapFeature.Require(ALLOWS_NULL_KEY_QUERIES)
    public void testContains_nullNotContainedButAllowed() {
        assertFalse(
                getMap().containsKey(null),
                "containsKey(null) should return false");
    }

    @MapFeature.Require(absent = ALLOWS_NULL_KEY_QUERIES)
    public void testContains_nullNotContainedAndUnsupported() {
        expectNullKeyMissingWhenNullKeysUnsupported("containsKey(null) should return false or throw");
    }

    @MapFeature.Require(ALLOWS_NULL_KEYS)
    @CollectionSize.Require(absent = ZERO)
    public void testContains_nonNullWhenNullContained() {
        initMapWithNullKey();
        assertFalse(
                getMap().containsKey(k3()),
                "containsKey(notPresent) should return false");
    }

    @MapFeature.Require(ALLOWS_NULL_KEYS)
    @CollectionSize.Require(absent = ZERO)
    public void testContains_nullContained() {
        initMapWithNullKey();
        assertTrue(
                getMap().containsKey(null),
                "containsKey(null) should return true");
    }

    public void testContains_wrongType() {
        try {
            // noinspection SuspiciousMethodCalls
            assertFalse(
                    getMap().containsKey(WrongType.VALUE),
                    "containsKey(wrongType) should return false or throw");
        } catch (ClassCastException tolerated) {
        }
    }
}
