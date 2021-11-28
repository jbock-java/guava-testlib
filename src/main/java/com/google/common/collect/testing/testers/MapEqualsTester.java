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
import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;
import org.junit.Ignore;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import static com.google.common.collect.testing.features.MapFeature.ALLOWS_NULL_KEYS;
import static com.google.common.collect.testing.features.MapFeature.ALLOWS_NULL_VALUES;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests {@link java.util.Map#equals}.
 *
 * @author George van den Driessche
 * @author Chris Povirk
 */
@Ignore // Affects only Android test runner, which respects JUnit 4 annotations on JUnit 3 tests.
public class MapEqualsTester<K, V> extends AbstractMapTester<K, V> {
    public void testEquals_otherMapWithSameEntries() {
        assertTrue(
                getMap().equals(newHashMap(getSampleEntries())),
                "A Map should equal any other Map containing the same entries.");
    }

    @CollectionSize.Require(absent = CollectionSize.ZERO)
    public void testEquals_otherMapWithDifferentEntries() {
        Map<K, V> other = newHashMap(getSampleEntries(getNumEntries() - 1));
        other.put(k3(), v3());
        assertFalse(
                getMap().equals(other),
                "A Map should not equal another Map containing different entries.");
    }

    @CollectionSize.Require(absent = CollectionSize.ZERO)
    @MapFeature.Require(ALLOWS_NULL_KEYS)
    public void testEquals_containingNullKey() {
        Collection<Entry<K, V>> entries = getSampleEntries(getNumEntries() - 1);
        entries.add(entry(null, v3()));

        resetContainer(getSubjectGenerator().create(entries.toArray()));
        assertTrue(
                getMap().equals(newHashMap(entries)),
                "A Map should equal any other Map containing the same entries,"
                        + " even if some keys are null.");
    }

    @CollectionSize.Require(absent = CollectionSize.ZERO)
    public void testEquals_otherContainsNullKey() {
        Collection<Entry<K, V>> entries = getSampleEntries(getNumEntries() - 1);
        entries.add(entry(null, v3()));
        Map<K, V> other = newHashMap(entries);

        assertFalse(
                getMap().equals(other),
                "Two Maps should not be equal if exactly one of them contains a null key.");
    }

    @CollectionSize.Require(absent = CollectionSize.ZERO)
    @MapFeature.Require(ALLOWS_NULL_VALUES)
    public void testEquals_containingNullValue() {
        Collection<Entry<K, V>> entries = getSampleEntries(getNumEntries() - 1);
        entries.add(entry(k3(), null));

        resetContainer(getSubjectGenerator().create(entries.toArray()));
        assertTrue(
                getMap().equals(newHashMap(entries)),
                "A Map should equal any other Map containing the same entries,"
                        + " even if some values are null.");
    }

    @CollectionSize.Require(absent = CollectionSize.ZERO)
    public void testEquals_otherContainsNullValue() {
        Collection<Entry<K, V>> entries = getSampleEntries(getNumEntries() - 1);
        entries.add(entry(k3(), null));
        Map<K, V> other = newHashMap(entries);

        assertFalse(
                getMap().equals(other),
                "Two Maps should not be equal if exactly one of them contains a null value.");
    }

    @CollectionSize.Require(absent = CollectionSize.ZERO)
    public void testEquals_smallerMap() {
        Collection<Entry<K, V>> fewerEntries = getSampleEntries(getNumEntries() - 1);
        assertFalse(
                getMap().equals(newHashMap(fewerEntries)),
                "Maps of different sizes should not be equal.");
    }

    public void testEquals_largerMap() {
        Collection<Entry<K, V>> moreEntries = getSampleEntries(getNumEntries() + 1);
        assertFalse(
                getMap().equals(newHashMap(moreEntries)),
                "Maps of different sizes should not be equal.");
    }

    public void testEquals_list() {
        assertFalse(
                getMap().equals(Helpers.copyToList(getMap().entrySet())),
                "A List should never equal a Map.");
    }

    private static <K, V> HashMap<K, V> newHashMap(
            Collection<? extends Entry<? extends K, ? extends V>> entries) {
        HashMap<K, V> map = new HashMap<>();
        for (Entry<? extends K, ? extends V> entry : entries) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }
}
