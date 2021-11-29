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

import com.google.common.collect.testing.MinimalCollection;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_ADD;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A generic JUnit test which tests addAll operations on a set. Can't be invoked directly; please
 * see {@link com.google.common.collect.testing.SetTestSuiteBuilder}.
 *
 * @author Kevin Bourrillion
 */
@SuppressWarnings("unchecked") // too many "unchecked generic array creations"
public class SetAddAllTester<E> extends AbstractSetTester<E> {
    @CollectionFeature.Require(SUPPORTS_ADD)
    @CollectionSize.Require(absent = ZERO)
    public void testAddAll_supportedSomePresent() {
        assertTrue(
                getSet().addAll(MinimalCollection.of(e3(), e0())),
                "add(somePresent) should return true");
        expectAdded(e3());
    }

    @CollectionFeature.Require(SUPPORTS_ADD)
    public void testAddAll_withDuplicates() {
        MinimalCollection<E> elementsToAdd = MinimalCollection.of(e3(), e4(), e3(), e4());
        assertTrue(
                getSet().addAll(elementsToAdd),
                "add(hasDuplicates) should return true");
        expectAdded(e3(), e4());
    }

    @CollectionFeature.Require(SUPPORTS_ADD)
    @CollectionSize.Require(absent = ZERO)
    public void testAddAll_supportedAllPresent() {
        assertFalse(
                getSet().addAll(MinimalCollection.of(e0())),
                "add(allPresent) should return false");
        expectUnchanged();
    }
}
