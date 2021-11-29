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

import static com.google.common.collect.testing.features.CollectionFeature.KNOWN_ORDER;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.SEVERAL;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * A generic JUnit test which tests {@code peek()} operations on a queue. Can't be invoked directly;
 * please see {@link com.google.common.collect.testing.CollectionTestSuiteBuilder}.
 *
 * @author Jared Levy
 */
public class QueuePeekTester<E> extends AbstractQueueTester<E> {
    @CollectionSize.Require(ZERO)
    public void testPeek_empty() {
        assertNull(
                getQueue().peek(),
                "emptyQueue.peek() should return null");
        expectUnchanged();
    }

    @CollectionSize.Require(ONE)
    public void testPeek_size1() {
        assertEquals(
                e0(),
                getQueue().peek(),
                "size1Queue.peek() should return first element");
        expectUnchanged();
    }

    @CollectionFeature.Require(KNOWN_ORDER)
    @CollectionSize.Require(SEVERAL)
    public void testPeek_sizeMany() {
        assertEquals(
                e0(),
                getQueue().peek(),
                "sizeManyQueue.peek() should return first element");
        expectUnchanged();
    }
}
