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

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.MinimalSet;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import java.util.Collection;
import java.util.Set;

import static com.google.common.collect.testing.features.CollectionFeature.ALLOWS_NULL_VALUES;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests {@link java.util.Set#equals}.
 *
 * @author George van den Driessche
 */
public class SetEqualsTester<E> extends AbstractSetTester<E> {
    public void testEquals_otherSetWithSameElements() {
        assertTrue(
                getSet().equals(MinimalSet.from(getSampleElements())),
                "A Set should equal any other Set containing the same elements.");
    }

    @CollectionSize.Require(absent = CollectionSize.ZERO)
    public void testEquals_otherSetWithDifferentElements() {
        Collection<E> elements = getSampleElements(getNumElements() - 1);
        elements.add(getSubjectGenerator().samples().e3());

        assertFalse(
                getSet().equals(MinimalSet.from(elements)),
                "A Set should not equal another Set containing different elements.");
    }

    @CollectionSize.Require(absent = CollectionSize.ZERO)
    @CollectionFeature.Require(ALLOWS_NULL_VALUES)
    public void testEquals_containingNull() {
        Collection<E> elements = getSampleElements(getNumElements() - 1);
        elements.add(null);

        collection = getSubjectGenerator().create(elements.toArray());
        assertTrue(
                getSet().equals(MinimalSet.from(elements)),
                "A Set should equal any other Set containing the same elements,"
                        + " even if some elements are null.");
    }

    @CollectionSize.Require(absent = CollectionSize.ZERO)
    public void testEquals_otherContainsNull() {
        Collection<E> elements = getSampleElements(getNumElements() - 1);
        elements.add(null);
        Set<E> other = MinimalSet.from(elements);

        assertFalse(
                getSet().equals(other),
                "Two Sets should not be equal if exactly one of them contains null.");
    }

    @CollectionSize.Require(absent = CollectionSize.ZERO)
    public void testEquals_smallerSet() {
        Collection<E> fewerElements = getSampleElements(getNumElements() - 1);
        assertFalse(
                getSet().equals(MinimalSet.from(fewerElements)),
                "Sets of different sizes should not be equal.");
    }

    public void testEquals_largerSet() {
        Collection<E> moreElements = getSampleElements(getNumElements() + 1);
        assertFalse(
                getSet().equals(MinimalSet.from(moreElements)),
                "Sets of different sizes should not be equal.");
    }

    public void testEquals_list() {
        assertFalse(
                getSet().equals(Helpers.copyToList(getSet())),
                "A List should never equal a Set.");
    }
}
