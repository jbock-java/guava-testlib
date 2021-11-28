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

import com.google.common.collect.testing.AbstractCollectionTester;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import org.junit.Ignore;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.function.Predicate;

import static com.google.common.collect.testing.features.CollectionFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION;
import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_ITERATOR_REMOVE;
import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * A generic JUnit test which tests {@link Collection#removeIf}. Can't be invoked directly; please
 * see {@link com.google.common.collect.testing.CollectionTestSuiteBuilder}.
 *
 * @author Louis Wasserman
 */
@SuppressWarnings("unchecked") // too many "unchecked generic array creations"
@Ignore // Affects only Android test runner, which respects JUnit 4 annotations on JUnit 3 tests.
public class CollectionRemoveIfTester<E> extends AbstractCollectionTester<E> {
    @CollectionFeature.Require(SUPPORTS_ITERATOR_REMOVE)
    public void testRemoveIf_alwaysFalse() {
        assertFalse(
                collection.removeIf(x -> false),
                "removeIf(x -> false) should return false");
        expectUnchanged();
    }

    @CollectionFeature.Require(SUPPORTS_ITERATOR_REMOVE)
    @CollectionSize.Require(absent = ZERO)
    public void testRemoveIf_sometimesTrue() {
        assertTrue(
                collection.removeIf(Predicate.isEqual(samples.e0())),
                "removeIf(isEqual(present)) should return true");
        expectMissing(samples.e0());
    }

    @CollectionFeature.Require(SUPPORTS_ITERATOR_REMOVE)
    @CollectionSize.Require(absent = ZERO)
    public void testRemoveIf_allPresent() {
        assertTrue(
                collection.removeIf(x -> true),
                "removeIf(x -> true) should return true");
        expectContents();
    }

    @CollectionFeature.Require({SUPPORTS_ITERATOR_REMOVE, FAILS_FAST_ON_CONCURRENT_MODIFICATION})
    @CollectionSize.Require(SEVERAL)
    public void testRemoveIfSomeMatchesConcurrentWithIteration() {
        try {
            Iterator<E> iterator = collection.iterator();
            assertTrue(collection.removeIf(Predicate.isEqual(samples.e0())));
            iterator.next();
            fail("Expected ConcurrentModificationException");
        } catch (ConcurrentModificationException expected) {
            // success
        }
    }

    @CollectionFeature.Require(absent = SUPPORTS_REMOVE)
    @CollectionSize.Require(ZERO)
    public void testRemoveIf_unsupportedEmptyCollection() {
        try {
            assertFalse(
                    collection.removeIf(
                            x -> {
                                throw new AssertionError("predicate should never be called");
                            }),
                    "removeIf(Predicate) should return false or throw " + "UnsupportedOperationException");
        } catch (UnsupportedOperationException tolerated) {
        }
        expectUnchanged();
    }

    @CollectionFeature.Require(absent = SUPPORTS_REMOVE)
    @CollectionSize.Require(absent = ZERO)
    public void testRemoveIf_alwaysTrueUnsupported() {
        try {
            collection.removeIf(x -> true);
            fail("removeIf(x -> true) should throw " + "UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
        }
        expectUnchanged();
        assertTrue(collection.contains(samples.e0()));
    }
}
