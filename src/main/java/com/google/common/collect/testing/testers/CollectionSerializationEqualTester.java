/*
 * Copyright (C) 2012 The Guava Authors
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
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.testing.SerializableTester;

import static com.google.common.collect.testing.features.CollectionFeature.SERIALIZABLE;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Basic reserialization test for collection types that must preserve {@code equals()} behavior when
 * reserialized. (Sets and Lists, but not bare Collections.)
 *
 * @author Louis Wasserman
 */
public class CollectionSerializationEqualTester<E> extends AbstractCollectionTester<E> {
    @CollectionFeature.Require(SERIALIZABLE)
    public void testReserialize() {
        assertEquals(SerializableTester.reserialize(actualContents()), actualContents());
    }
}
