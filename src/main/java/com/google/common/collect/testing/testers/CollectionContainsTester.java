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

import com.google.common.collect.testing.AbstractCollectionTester;
import com.google.common.collect.testing.WrongType;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.ALLOWS_NULL_QUERIES;
import static com.google.common.collect.testing.features.CollectionFeature.ALLOWS_NULL_VALUES;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A generic JUnit test which tests {@code contains()} operations on a collection. Can't be invoked
 * directly; please see {@link com.google.common.collect.testing.CollectionTestSuiteBuilder}.
 *
 * @author Kevin Bourrillion
 * @author Chris Povirk
 */
@Ignore // Affects only Android test runner, which respects JUnit 4 annotations on JUnit 3 tests.
public class CollectionContainsTester<E> extends AbstractCollectionTester<E> {
    @CollectionSize.Require(absent = ZERO)
    public void testContains_yes() {
        assertTrue(
                collection.contains(e0()),
                "contains(present) should return true");
    }

    public void testContains_no() {
        assertFalse(
                collection.contains(e3()),
                "contains(notPresent) should return false");
    }

    @CollectionFeature.Require(ALLOWS_NULL_QUERIES)
    public void testContains_nullNotContainedButQueriesSupported() {
        assertFalse(
                collection.contains(null),
                "contains(null) should return false");
    }

    @CollectionFeature.Require(absent = ALLOWS_NULL_QUERIES)
    public void testContains_nullNotContainedAndUnsupported() {
        expectNullMissingWhenNullUnsupported("contains(null) should return false or throw");
    }

    @CollectionFeature.Require(ALLOWS_NULL_VALUES)
    @CollectionSize.Require(absent = ZERO)
    public void testContains_nonNullWhenNullContained() {
        initCollectionWithNullElement();
        assertFalse(
                collection.contains(e3()),
                "contains(notPresent) should return false");
    }

    @CollectionFeature.Require(ALLOWS_NULL_VALUES)
    @CollectionSize.Require(absent = ZERO)
    public void testContains_nullContained() {
        initCollectionWithNullElement();
        assertTrue(
                collection.contains(null),
                "contains(null) should return true");
    }

    public void testContains_wrongType() {
        try {
            assertFalse(
                    collection.contains(WrongType.VALUE),
                    "contains(wrongType) should return false or throw");
        } catch (ClassCastException tolerated) {
        }
    }
}
