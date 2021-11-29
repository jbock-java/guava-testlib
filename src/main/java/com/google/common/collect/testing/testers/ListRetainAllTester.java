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

import com.google.common.collect.testing.MinimalCollection;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import java.util.Arrays;

import static com.google.common.collect.testing.Helpers.assertContentsInOrder;
import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A generic JUnit test which tests {@code retainAll} operations on a list. Can't be invoked
 * directly; please see {@link com.google.common.collect.testing.ListTestSuiteBuilder}.
 *
 * @author Chris Povirk
 */
public class ListRetainAllTester<E> extends AbstractListTester<E> {
    @CollectionFeature.Require(SUPPORTS_REMOVE)
    @CollectionSize.Require(absent = {ZERO, ONE})
    public void testRetainAll_duplicatesKept() {
        E[] array = createSamplesArray();
        array[1] = e0();
        collection = getSubjectGenerator().create(array);
        assertFalse(
                collection.retainAll(MinimalCollection.of(createSamplesArray())),
                "containsDuplicates.retainAll(superset) should return false");
        expectContents(array);
    }

    @SuppressWarnings("unchecked")
    @CollectionFeature.Require(SUPPORTS_REMOVE)
    @CollectionSize.Require(SEVERAL)
    public void testRetainAll_duplicatesRemoved() {
        E[] array = createSamplesArray();
        array[1] = e0();
        collection = getSubjectGenerator().create(array);
        assertTrue(
                collection.retainAll(MinimalCollection.of(e2())),
                "containsDuplicates.retainAll(subset) should return true");
        expectContents(e2());
    }

    @SuppressWarnings("unchecked")
    @CollectionFeature.Require(SUPPORTS_REMOVE)
    @CollectionSize.Require(SEVERAL)
    public void testRetainAll_countIgnored() {
        resetContainer(getSubjectGenerator().create(e0(), e2(), e1(), e0()));
        assertTrue(getList().retainAll(Arrays.asList(e0(), e1())));
        assertContentsInOrder(getList(), e0(), e1(), e0());
    }
}
