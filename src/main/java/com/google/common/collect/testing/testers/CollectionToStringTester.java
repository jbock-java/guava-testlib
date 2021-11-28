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
import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.ALLOWS_NULL_VALUES;
import static com.google.common.collect.testing.features.CollectionFeature.KNOWN_ORDER;
import static com.google.common.collect.testing.features.CollectionFeature.NON_STANDARD_TOSTRING;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * A generic JUnit test which tests {@code toString()} operations on a collection. Can't be invoked
 * directly; please see {@link com.google.common.collect.testing.CollectionTestSuiteBuilder}.
 *
 * @author Kevin Bourrillion
 */
@Ignore // Affects only Android test runner, which respects JUnit 4 annotations on JUnit 3 tests.
public class CollectionToStringTester<E> extends AbstractCollectionTester<E> {
    public void testToString_minimal() {
        assertNotNull(
                collection.toString(),
                "toString() should not return null");
    }

    @CollectionSize.Require(ZERO)
    @CollectionFeature.Require(absent = NON_STANDARD_TOSTRING)
    public void testToString_size0() {
        assertEquals(
                "[]",
                collection.toString(),
                "emptyCollection.toString should return []");
    }

    @CollectionSize.Require(ONE)
    @CollectionFeature.Require(absent = NON_STANDARD_TOSTRING)
    public void testToString_size1() {
        assertEquals(
                "[" + e0() + "]",
                collection.toString(),
                "size1Collection.toString should return [{element}]");
    }

    @CollectionSize.Require(SEVERAL)
    @CollectionFeature.Require(value = KNOWN_ORDER, absent = NON_STANDARD_TOSTRING)
    public void testToString_sizeSeveral() {
        String expected = Helpers.copyToList(getOrderedElements()).toString();
        assertEquals(
                expected,
                collection.toString(),
                "collection.toString() incorrect");
    }

    @CollectionSize.Require(absent = ZERO)
    @CollectionFeature.Require(ALLOWS_NULL_VALUES)
    public void testToString_null() {
        initCollectionWithNullElement();
        testToString_minimal();
    }
}
