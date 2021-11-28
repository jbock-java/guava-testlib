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
import org.junit.Ignore;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests {@link java.util.Collection#equals}.
 *
 * @author George van den Driessche
 */
@Ignore // Affects only Android test runner, which respects JUnit 4 annotations on JUnit 3 tests.
public class CollectionEqualsTester<E> extends AbstractCollectionTester<E> {

    // TODO(cpovirk): Consider using EqualsTester from Guava.
    @SuppressWarnings("SelfEquals")
    public void testEquals_self() {
        assertTrue(
                collection.equals(collection),
                "An Object should be equal to itself.");
    }

    public void testEquals_null() {
        // noinspection ObjectEqualsNull
        assertFalse(
                collection.equals(null),
                "An object should not be equal to null.");
    }

    public void testEquals_notACollection() {
        // noinspection EqualsBetweenInconvertibleTypes
        assertFalse(
                collection.equals("huh?"),
                "A Collection should never equal an object that is not a Collection.");
    }
}
