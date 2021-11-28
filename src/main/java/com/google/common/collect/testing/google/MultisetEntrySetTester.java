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

package com.google.common.collect.testing.google;

import com.google.common.collect.Iterables;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import org.junit.Ignore;

import java.util.Collections;
import java.util.Iterator;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_ADD;
import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_ITERATOR_REMOVE;
import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.google.MultisetFeature.ENTRIES_ARE_VIEWS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@code Multiset.entrySet}.
 *
 * @author Jared Levy
 */
@Ignore // Affects only Android test runner, which respects JUnit 4 annotations on JUnit 3 tests.
public class MultisetEntrySetTester<E> extends AbstractMultisetTester<E> {

    @CollectionFeature.Require(SUPPORTS_REMOVE)
    public void testEntrySet_clear() {
        getMultiset().entrySet().clear();
        assertTrue(
                getMultiset().isEmpty(),
                "multiset not empty after entrySet().clear()");
    }

    @CollectionSize.Require(ONE)
    @CollectionFeature.Require(SUPPORTS_ITERATOR_REMOVE)
    public void testEntrySet_iteratorRemovePropagates() {
        Iterator<Multiset.Entry<E>> iterator = getMultiset().entrySet().iterator();
        assertTrue(
                iterator.hasNext(),
                "non-empty multiset.entrySet() iterator.hasNext() returned false");
        assertEquals(
                Multisets.immutableEntry(e0(), 1),
                iterator.next(),
                "multiset.entrySet() iterator.next() returned incorrect entry");
        assertFalse(
                iterator.hasNext(),
                "size 1 multiset.entrySet() iterator.hasNext() returned true after next()");
        iterator.remove();
        assertTrue(
                getMultiset().isEmpty(),
                "multiset isn't empty after multiset.entrySet() iterator.remove()");
    }

    @CollectionSize.Require(absent = ZERO)
    @CollectionFeature.Require(SUPPORTS_REMOVE)
    public void testEntrySet_removePresent() {
        assertTrue(
                getMultiset().entrySet().remove(Multisets.immutableEntry(e0(), 1)),
                "multiset.entrySet.remove(presentEntry) returned false");
        assertFalse(
                getMultiset().contains(e0()),
                "multiset contains element after removing its entry");
    }

    @CollectionSize.Require(absent = ZERO)
    @CollectionFeature.Require(SUPPORTS_REMOVE)
    public void testEntrySet_removeAbsent() {
        assertFalse(
                getMultiset().entrySet().remove(Multisets.immutableEntry(e0(), 2)),
                "multiset.entrySet.remove(missingEntry) returned true");
        assertTrue(
                getMultiset().contains(e0()),
                "multiset didn't contain element after removing a missing entry");
    }

    @CollectionSize.Require(absent = ZERO)
    @CollectionFeature.Require(SUPPORTS_REMOVE)
    public void testEntrySet_removeAllPresent() {
        assertTrue(
                getMultiset()
                        .entrySet()
                        .removeAll(Collections.singleton(Multisets.immutableEntry(e0(), 1))),
                "multiset.entrySet.removeAll(presentEntry) returned false");
        assertFalse(
                getMultiset().contains(e0()),
                "multiset contains element after removing its entry");
    }

    @CollectionSize.Require(absent = ZERO)
    @CollectionFeature.Require(SUPPORTS_REMOVE)
    public void testEntrySet_removeAllAbsent() {
        assertFalse(
                getMultiset()
                        .entrySet()
                        .removeAll(Collections.singleton(Multisets.immutableEntry(e0(), 2))),
                "multiset.entrySet.remove(missingEntry) returned true");
        assertTrue(
                getMultiset().contains(e0()),
                "multiset didn't contain element after removing a missing entry");
    }

    @CollectionSize.Require(ONE)
    @CollectionFeature.Require(SUPPORTS_REMOVE)
    public void testEntrySet_retainAllPresent() {
        assertFalse(
                getMultiset()
                        .entrySet()
                        .retainAll(Collections.singleton(Multisets.immutableEntry(e0(), 1))),
                "multiset.entrySet.retainAll(presentEntry) returned false");
        assertTrue(
                getMultiset().contains(e0()),
                "multiset doesn't contains element after retaining its entry");
    }

    @CollectionSize.Require(ONE)
    @CollectionFeature.Require(SUPPORTS_REMOVE)
    public void testEntrySet_retainAllAbsent() {
        assertTrue(
                getMultiset()
                        .entrySet()
                        .retainAll(Collections.singleton(Multisets.immutableEntry(e0(), 2))),
                "multiset.entrySet.retainAll(missingEntry) returned true");
        assertFalse(
                getMultiset().contains(e0()),
                "multiset contains element after retaining a different entry");
    }

    @CollectionSize.Require(SEVERAL)
    @CollectionFeature.Require(SUPPORTS_REMOVE)
    @MultisetFeature.Require(ENTRIES_ARE_VIEWS)
    public void testEntryViewReflectsRemove() {
        initThreeCopies();
        assertEquals(3, getMultiset().count(e0()));
        Multiset.Entry<E> entry = Iterables.getOnlyElement(getMultiset().entrySet());
        assertEquals(3, entry.getCount());
        assertTrue(getMultiset().remove(e0()));
        assertEquals(2, entry.getCount());
        assertTrue(getMultiset().elementSet().remove(e0()));
        assertEquals(0, entry.getCount());
    }

    @CollectionSize.Require(SEVERAL)
    @CollectionFeature.Require(SUPPORTS_ITERATOR_REMOVE)
    @MultisetFeature.Require(ENTRIES_ARE_VIEWS)
    public void testEntryReflectsIteratorRemove() {
        initThreeCopies();
        assertEquals(3, getMultiset().count(e0()));
        Multiset.Entry<E> entry = Iterables.getOnlyElement(getMultiset().entrySet());
        assertEquals(3, entry.getCount());
        Iterator<E> itr = getMultiset().iterator();
        itr.next();
        itr.remove();
        assertEquals(2, entry.getCount());
        itr.next();
        itr.remove();
        itr.next();
        itr.remove();
        assertEquals(0, entry.getCount());
    }

    @CollectionSize.Require(SEVERAL)
    @CollectionFeature.Require(SUPPORTS_REMOVE)
    @MultisetFeature.Require(ENTRIES_ARE_VIEWS)
    public void testEntryReflectsClear() {
        initThreeCopies();
        assertEquals(3, getMultiset().count(e0()));
        Multiset.Entry<E> entry = Iterables.getOnlyElement(getMultiset().entrySet());
        assertEquals(3, entry.getCount());
        getMultiset().clear();
        assertEquals(0, entry.getCount());
    }

    @CollectionSize.Require(SEVERAL)
    @CollectionFeature.Require(SUPPORTS_REMOVE)
    @MultisetFeature.Require(ENTRIES_ARE_VIEWS)
    public void testEntryReflectsEntrySetClear() {
        initThreeCopies();
        assertEquals(3, getMultiset().count(e0()));
        Multiset.Entry<E> entry = Iterables.getOnlyElement(getMultiset().entrySet());
        assertEquals(3, entry.getCount());
        getMultiset().entrySet().clear();
        assertEquals(0, entry.getCount());
    }

    @CollectionSize.Require(SEVERAL)
    @CollectionFeature.Require(SUPPORTS_ITERATOR_REMOVE)
    @MultisetFeature.Require(ENTRIES_ARE_VIEWS)
    public void testEntryReflectsEntrySetIteratorRemove() {
        initThreeCopies();
        assertEquals(3, getMultiset().count(e0()));
        Iterator<Multiset.Entry<E>> entryItr = getMultiset().entrySet().iterator();
        Multiset.Entry<E> entry = entryItr.next();
        entryItr.remove();
        assertEquals(0, entry.getCount());
    }

    @CollectionSize.Require(SEVERAL)
    @CollectionFeature.Require(SUPPORTS_REMOVE)
    @MultisetFeature.Require(ENTRIES_ARE_VIEWS)
    public void testEntryReflectsElementSetClear() {
        initThreeCopies();
        assertEquals(3, getMultiset().count(e0()));
        Multiset.Entry<E> entry = Iterables.getOnlyElement(getMultiset().entrySet());
        assertEquals(3, entry.getCount());
        getMultiset().elementSet().clear();
        assertEquals(0, entry.getCount());
    }

    @CollectionSize.Require(SEVERAL)
    @CollectionFeature.Require(SUPPORTS_ITERATOR_REMOVE)
    @MultisetFeature.Require(ENTRIES_ARE_VIEWS)
    public void testEntryReflectsElementSetIteratorRemove() {
        initThreeCopies();
        assertEquals(3, getMultiset().count(e0()));
        Multiset.Entry<E> entry = Iterables.getOnlyElement(getMultiset().entrySet());
        assertEquals(3, entry.getCount());
        Iterator<E> elementItr = getMultiset().elementSet().iterator();
        elementItr.next();
        elementItr.remove();
        assertEquals(0, entry.getCount());
    }

    @CollectionSize.Require(SEVERAL)
    @CollectionFeature.Require({SUPPORTS_REMOVE, SUPPORTS_ADD})
    @MultisetFeature.Require(ENTRIES_ARE_VIEWS)
    public void testEntryReflectsRemoveThenAdd() {
        initThreeCopies();
        assertEquals(3, getMultiset().count(e0()));
        Multiset.Entry<E> entry = Iterables.getOnlyElement(getMultiset().entrySet());
        assertEquals(3, entry.getCount());
        assertTrue(getMultiset().remove(e0()));
        assertEquals(2, entry.getCount());
        assertTrue(getMultiset().elementSet().remove(e0()));
        assertEquals(0, entry.getCount());
        getMultiset().add(e0(), 2);
        assertEquals(2, entry.getCount());
    }

    public void testToString() {
        assertEquals(getMultiset().entrySet().toString(), getMultiset().toString());
    }
}
