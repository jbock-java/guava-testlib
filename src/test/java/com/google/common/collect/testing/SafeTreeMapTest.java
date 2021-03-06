/*
 * Copyright (C) 2010 The Guava Authors
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

import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.testing.Helpers.NullsBeforeTwo;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;
import com.google.common.testing.SerializableTester;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.SortedMap;

import static java.util.Collections.sort;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for SafeTreeMap.
 *
 * @author Louis Wasserman
 */
class SafeTreeMapTest {

    @Test
    void testWithNaturalComparator() throws Throwable {
        NavigableMapTestSuiteBuilder.using(
                        new TestStringSortedMapGenerator() {
                            @Override
                            protected SortedMap<String, String> create(Entry<String, String>[] entries) {
                                NavigableMap<String, String> map = new SafeTreeMap<>(Ordering.natural());
                                for (Entry<String, String> entry : entries) {
                                    map.put(entry.getKey(), entry.getValue());
                                }
                                return map;
                            }
                        })
                .withFeatures(
                        CollectionSize.ANY,
                        CollectionFeature.KNOWN_ORDER,
                        CollectionFeature.SERIALIZABLE,
                        MapFeature.ALLOWS_NULL_VALUES,
                        CollectionFeature.SUPPORTS_ITERATOR_REMOVE,
                        MapFeature.GENERAL_PURPOSE)
                .named("SafeTreeMap with natural comparator")
                .createTestSuite()
                .run();
    }

    @Test
    void testWithNullFriendlyComparator() throws Throwable {
        NavigableMapTestSuiteBuilder.using(
                        new TestStringSortedMapGenerator() {
                            @Override
                            protected SortedMap<String, String> create(Entry<String, String>[] entries) {
                                NavigableMap<String, String> map = new SafeTreeMap<>(NullsBeforeTwo.INSTANCE);
                                for (Entry<String, String> entry : entries) {
                                    map.put(entry.getKey(), entry.getValue());
                                }
                                return map;
                            }

                            @Override
                            public Iterable<Entry<String, String>> order(
                                    List<Entry<String, String>> insertionOrder) {
                                sort(insertionOrder,
                                        Helpers.entryComparator(NullsBeforeTwo.INSTANCE));
                                return insertionOrder;
                            }
                        })
                .withFeatures(
                        CollectionSize.ANY,
                        CollectionFeature.KNOWN_ORDER,
                        MapFeature.ALLOWS_NULL_KEYS,
                        MapFeature.ALLOWS_NULL_VALUES,
                        MapFeature.ALLOWS_ANY_NULL_QUERIES,
                        MapFeature.GENERAL_PURPOSE,
                        CollectionFeature.SUPPORTS_ITERATOR_REMOVE,
                        CollectionFeature.SERIALIZABLE)
                .named("SafeTreeMap with null-friendly comparator")
                .createTestSuite()
                .run();
    }

    @Test
    void testViewSerialization() {
        Map<String, Integer> map = ImmutableSortedMap.of("one", 1, "two", 2, "three", 3);
        SerializableTester.reserializeAndAssert(map.entrySet());
        SerializableTester.reserializeAndAssert(map.keySet());
        assertEquals(
                Lists.newArrayList(map.values()),
                Lists.newArrayList(SerializableTester.reserialize(map.values())));
    }

    @Test
    void reserializableMapTest() {
        new SortedMapInterfaceTest<String, Integer>(false, true, true, true, true) {
            @Override
            protected SortedMap<String, Integer> makePopulatedMap() {
                NavigableMap<String, Integer> map = new SafeTreeMap<>();
                map.put("one", 1);
                map.put("two", 2);
                map.put("three", 3);
                return SerializableTester.reserialize(map);
            }

            @Override
            protected SortedMap<String, Integer> makeEmptyMap() throws UnsupportedOperationException {
                NavigableMap<String, Integer> map = new SafeTreeMap<>();
                return SerializableTester.reserialize(map);
            }

            @Override
            protected String getKeyNotInPopulatedMap() {
                return "minus one";
            }

            @Override
            protected Integer getValueNotInPopulatedMap() {
                return -1;
            }
        }.runAllTests();
    }
}
