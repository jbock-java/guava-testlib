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
import com.google.common.collect.testing.features.CollectionSize;
import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A generic JUnit test which tests {@code isEmpty()} operations on a collection. Can't be invoked
 * directly; please see {@link com.google.common.collect.testing.CollectionTestSuiteBuilder}.
 *
 * @author Kevin Bourrillion
 */
@Ignore // Affects only Android test runner, which respects JUnit 4 annotations on JUnit 3 tests.
public class CollectionIsEmptyTester<E> extends AbstractCollectionTester<E> {
    @CollectionSize.Require(ZERO)
    public void testIsEmpty_yes() {
        assertTrue(
                collection.isEmpty(),
                "isEmpty() should return true");
    }

    @CollectionSize.Require(absent = ZERO)
    public void testIsEmpty_no() {
        assertFalse(
                collection.isEmpty(),
                "isEmpty() should return false");
    }
}
