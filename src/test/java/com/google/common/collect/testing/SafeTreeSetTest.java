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
import com.google.common.collect.Sets;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.testing.SerializableTester;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SafeTreeSetTest {

    @Test
    void testWithNaturalComparator() throws Throwable {
        NavigableSetTestSuiteBuilder.using(
                        new TestStringSetGenerator() {
                            @Override
                            protected Set<String> create(String[] elements) {
                                return new SafeTreeSet<>(Arrays.asList(elements));
                            }

                            @Override
                            public List<String> order(List<String> insertionOrder) {
                                return Lists.newArrayList(Sets.newTreeSet(insertionOrder));
                            }
                        })
                .withFeatures(
                        CollectionSize.ANY,
                        CollectionFeature.KNOWN_ORDER,
                        CollectionFeature.GENERAL_PURPOSE)
                .named("SafeTreeSet with natural comparator")
                .createTestSuite()
                .run();
    }

    @Test
    void testWithNullFriendlyComparator() throws Throwable {
        SetTestSuiteBuilder.using(
                        new TestStringSetGenerator() {
                            @Override
                            protected Set<String> create(String[] elements) {
                                NavigableSet<String> set = new SafeTreeSet<>(Ordering.natural().nullsFirst());
                                Collections.addAll(set, elements);
                                return set;
                            }

                            @Override
                            public List<String> order(List<String> insertionOrder) {
                                return Lists.newArrayList(Sets.newTreeSet(insertionOrder));
                            }
                        })
                .withFeatures(
                        CollectionSize.ANY,
                        CollectionFeature.KNOWN_ORDER,
                        CollectionFeature.GENERAL_PURPOSE,
                        CollectionFeature.ALLOWS_NULL_VALUES)
                .named("SafeTreeSet with null-friendly comparator")
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
    void testEmpty_serialization() {
        SortedSet<String> set = new SafeTreeSet<>();
        SortedSet<String> copy = SerializableTester.reserializeAndAssert(set);
        assertEquals(set.comparator(), copy.comparator());
    }

    @Test
    void testSingle_serialization() {
        SortedSet<String> set = new SafeTreeSet<>();
        set.add("e");
        SortedSet<String> copy = SerializableTester.reserializeAndAssert(set);
        assertEquals(set.comparator(), copy.comparator());
    }

    @Test
    void testSeveral_serialization() {
        SortedSet<String> set = new SafeTreeSet<>();
        set.add("a");
        set.add("b");
        set.add("c");
        SortedSet<String> copy = SerializableTester.reserializeAndAssert(set);
        assertEquals(set.comparator(), copy.comparator());
    }
}
