/*
 * Copyright (C) 2007 The Guava Authors
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * A generic JUnit test which tests {@code get} operations on a map. Can't be invoked directly;
 * please see {@link com.google.common.collect.testing.MapTestSuiteBuilder}.
 *
 * @author Kevin Bourrillion
 * @author Chris Povirk
 */
public class MapGetTester<K, V> extends AbstractMapTester<K, V> {
    @CollectionSize.Require(absent = ZERO)
    public void testGet_yes() {
        assertEquals(
                v0(),
                get(k0()),
                "get(present) should return the associated value");
    }

    public void testGet_no() {
        assertNull(
                get(k3()),
                "get(notPresent) should return null");
    }

    @MapFeature.Require(ALLOWS_NULL_KEY_QUERIES)
    public void testGet_nullNotContainedButAllowed() {
        assertNull(
                get(null),
                "get(null) should return null");
    }

    @MapFeature.Require(absent = ALLOWS_NULL_KEY_QUERIES)
    public void testGet_nullNotContainedAndUnsupported() {
        try {
            assertNull(
                    get(null),
                    "get(null) should return null or throw");
        } catch (NullPointerException tolerated) {
        }
    }

    @MapFeature.Require(ALLOWS_NULL_KEYS)
    @CollectionSize.Require(absent = ZERO)
    public void testGet_nonNullWhenNullContained() {
        initMapWithNullKey();
        assertNull(
                get(k3()),
                "get(notPresent) should return null");
    }

    @MapFeature.Require(ALLOWS_NULL_KEYS)
    @CollectionSize.Require(absent = ZERO)
    public void testGet_nullContained() {
        initMapWithNullKey();
        assertEquals(
                getValueForNullKey(),
                get(null),
                "get(null) should return the associated value");
    }

    public void testGet_wrongType() {
        try {
            assertNull(
                    getMap().get(WrongType.VALUE),
                    "get(wrongType) should return null or throw");
        } catch (ClassCastException tolerated) {
        }
    }
}
