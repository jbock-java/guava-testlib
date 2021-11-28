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

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A generic JUnit test which tests {@code remove(Object)} operations on a list. Can't be invoked
 * directly; please see {@link com.google.common.collect.testing.ListTestSuiteBuilder}.
 *
 * @author George van den Driessche
 */
@Ignore // Affects only Android test runner, which respects JUnit 4 annotations on JUnit 3 tests.
public class ListRemoveTester<E> extends AbstractListTester<E> {
    @CollectionFeature.Require(SUPPORTS_REMOVE)
    @CollectionSize.Require(absent = {ZERO, ONE})
    public void testRemove_duplicate() {
        ArrayWithDuplicate<E> arrayAndDuplicate = createArrayWithDuplicateElement();
        collection = getSubjectGenerator().create(arrayAndDuplicate.elements);
        E duplicate = arrayAndDuplicate.duplicate;

        int firstIndex = getList().indexOf(duplicate);
        int initialSize = getList().size();
        assertTrue(
                getList().remove(duplicate),
                "remove(present) should return true");
        assertTrue(
                getList().contains(duplicate),
                "After remove(duplicate), a list should still contain the duplicate element");
        assertFalse(
                firstIndex == getList().indexOf(duplicate),
                "remove(duplicate) should remove the first instance of the "
                        + "duplicate element in the list");
        assertEquals(
                initialSize - 1,
                getList().size(),
                "remove(present) should decrease the size of a list by one.");
    }
}
