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

import com.google.common.collect.testing.WrongType;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.ALLOWS_NULL_VALUES;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Common parent class for {@link ListIndexOfTester} and {@link ListLastIndexOfTester}.
 *
 * @author Chris Povirk
 */
@Ignore // Affects only Android test runner, which respects JUnit 4 annotations on JUnit 3 tests.
public abstract class AbstractListIndexOfTester<E> extends AbstractListTester<E> {
    /** Override to call {@code indexOf()} or {@code lastIndexOf()}. */
    protected abstract int find(Object o);

    /** Override to return "indexOf" or "lastIndexOf()" for use in failure messages. */
    protected abstract String getMethodName();

    @CollectionSize.Require(absent = ZERO)
    public void testFind_yes() {
        assertEquals(
                0,
                find(getOrderedElements().get(0)),
                getMethodName() + "(firstElement) should return 0");
    }

    public void testFind_no() {
        assertEquals(
                -1,
                find(e3()),
                getMethodName() + "(notPresent) should return -1");
    }

    @CollectionFeature.Require(ALLOWS_NULL_VALUES)
    public void testFind_nullNotContainedButSupported() {
        assertEquals(
                -1,
                find(null),
                getMethodName() + "(nullNotPresent) should return -1");
    }

    @CollectionFeature.Require(absent = ALLOWS_NULL_VALUES)
    public void testFind_nullNotContainedAndUnsupported() {
        try {
            assertEquals(
                    -1,
                    find(null),
                    getMethodName() + "(nullNotPresent) should return -1 or throw");
        } catch (NullPointerException tolerated) {
        }
    }

    @CollectionFeature.Require(ALLOWS_NULL_VALUES)
    @CollectionSize.Require(absent = ZERO)
    public void testFind_nonNullWhenNullContained() {
        initCollectionWithNullElement();
        assertEquals(
                -1,
                find(e3()),
                getMethodName() + "(notPresent) should return -1");
    }

    @CollectionFeature.Require(ALLOWS_NULL_VALUES)
    @CollectionSize.Require(absent = ZERO)
    public void testFind_nullContained() {
        initCollectionWithNullElement();
        assertEquals(
                getNullLocation(),
                find(null),
                getMethodName() + "(null) should return " + getNullLocation());
    }

    public void testFind_wrongType() {
        try {
            assertEquals(
                    -1,
                    find(WrongType.VALUE),
                    getMethodName() + "(wrongType) should return -1 or throw");
        } catch (ClassCastException tolerated) {
        }
    }
}
