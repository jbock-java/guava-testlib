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
import com.google.common.collect.testing.WrongType;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import java.util.Map;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.ALLOWS_NULL_KEYS;
import static com.google.common.collect.testing.features.MapFeature.ALLOWS_NULL_KEY_QUERIES;
import static com.google.common.collect.testing.features.MapFeature.ALLOWS_NULL_VALUES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * A generic JUnit test which tests {@link Map#getOrDefault}. Can't be invoked directly; please see
 * {@link com.google.common.collect.testing.MapTestSuiteBuilder}.
 *
 * @author Louis Wasserman
 */
public class MapGetOrDefaultTester<K, V> extends AbstractMapTester<K, V> {
    @CollectionSize.Require(absent = ZERO)
    public void testGetOrDefault_present() {
        assertEquals(
                v0(),
                getMap().getOrDefault(k0(), v3()),
                "getOrDefault(present, def) should return the associated value");
    }

    @CollectionSize.Require(absent = ZERO)
    public void testGetOrDefault_presentNullDefault() {
        assertEquals(
                v0(),
                getMap().getOrDefault(k0(), null),
                "getOrDefault(present, null) should return the associated value");
    }

    public void testGetOrDefault_absent() {
        assertEquals(
                v3(),
                getMap().getOrDefault(k3(), v3()),
                "getOrDefault(absent, def) should return the default value");
    }

    public void testGetOrDefault_absentNullDefault() {
        assertNull(
                getMap().getOrDefault(k3(), null),
                "getOrDefault(absent, null) should return null");
    }

    @MapFeature.Require(ALLOWS_NULL_KEY_QUERIES)
    public void testGetOrDefault_absentNull() {
        assertEquals(
                v3(),
                getMap().getOrDefault(null, v3()),
                "getOrDefault(null, def) should return the default value");
    }

    @MapFeature.Require(absent = ALLOWS_NULL_KEY_QUERIES)
    public void testGetOrDefault_nullAbsentAndUnsupported() {
        try {
            assertEquals(
                    v3(),
                    getMap().getOrDefault(null, v3()),
                    "getOrDefault(null, def) should return default or throw");
        } catch (NullPointerException tolerated) {
        }
    }

    @MapFeature.Require(ALLOWS_NULL_KEYS)
    @CollectionSize.Require(absent = ZERO)
    public void testGetOrDefault_nonNullWhenNullContained() {
        initMapWithNullKey();
        assertEquals(
                v3(),
                getMap().getOrDefault(k3(), v3()),
                "getOrDefault(absent, default) should return default");
    }

    @MapFeature.Require(ALLOWS_NULL_KEYS)
    @CollectionSize.Require(absent = ZERO)
    public void testGetOrDefault_presentNull() {
        initMapWithNullKey();
        assertEquals(
                getValueForNullKey(),
                getMap().getOrDefault(null, v3()),
                "getOrDefault(null, default) should return the associated value");
    }

    @MapFeature.Require(ALLOWS_NULL_VALUES)
    @CollectionSize.Require(absent = ZERO)
    public void testGetOrDefault_presentMappedToNull() {
        initMapWithNullValue();
        assertNull(
                getMap().getOrDefault(getKeyForNullValue(), v3()),
                "getOrDefault(mappedToNull, default) should return null");
    }

    public void testGet_wrongType() {
        try {
            assertEquals(
                    v3(),
                    getMap().getOrDefault(WrongType.VALUE, v3()),
                    "getOrDefault(wrongType, default) should return default or throw");
        } catch (ClassCastException tolerated) {
        }
    }
}
