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

import com.google.common.collect.testing.MinimalSet;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.google.common.collect.testing.features.CollectionFeature.ALLOWS_NULL_VALUES;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests {@link List#equals}.
 *
 * @author George van den Driessche
 */
@Ignore // Affects only Android test runner, which respects JUnit 4 annotations on JUnit 3 tests.
public class ListEqualsTester<E> extends AbstractListTester<E> {
    public void testEquals_otherListWithSameElements() {
        assertTrue(
                getList().equals(new ArrayList<E>(getOrderedElements())),
                "A List should equal any other List containing the same elements.");
    }

    @CollectionSize.Require(absent = CollectionSize.ZERO)
    public void testEquals_otherListWithDifferentElements() {
        ArrayList<E> other = new ArrayList<>(getSampleElements());
        other.set(other.size() / 2, getSubjectGenerator().samples().e3());
        assertFalse(
                getList().equals(other),
                "A List should not equal another List containing different elements.");
    }

    @CollectionSize.Require(absent = CollectionSize.ZERO)
    public void testEquals_otherListContainingNull() {
        List<E> other = new ArrayList<>(getSampleElements());
        other.set(other.size() / 2, null);
        assertFalse(
                getList().equals(other),
                "Two Lists should not be equal if exactly one of them has null at a given index.");
    }

    @CollectionSize.Require(absent = CollectionSize.ZERO)
    @CollectionFeature.Require(ALLOWS_NULL_VALUES)
    public void testEquals_containingNull() {
        ArrayList<E> elements = new ArrayList<>(getSampleElements());
        elements.set(elements.size() / 2, null);
        collection = getSubjectGenerator().create(elements.toArray());
        List<E> other = new ArrayList<>(getSampleElements());
        assertFalse(
                getList().equals(other),
                "Two Lists should not be equal if exactly one of them has null at a given index.");
    }

    @CollectionSize.Require(absent = CollectionSize.ZERO)
    public void testEquals_shorterList() {
        Collection<E> fewerElements = getSampleElements(getNumElements() - 1);
        assertFalse(
                getList().equals(new ArrayList<E>(fewerElements)),
                "Lists of different sizes should not be equal.");
    }

    public void testEquals_longerList() {
        Collection<E> moreElements = getSampleElements(getNumElements() + 1);
        assertFalse(
                getList().equals(new ArrayList<E>(moreElements)),
                "Lists of different sizes should not be equal.");
    }

    public void testEquals_set() {
        assertFalse(
                getList().equals(MinimalSet.from(getList())),
                "A List should never equal a Set.");
    }
}
