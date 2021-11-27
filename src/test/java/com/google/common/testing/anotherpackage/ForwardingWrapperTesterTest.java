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

package com.google.common.testing.anotherpackage;

import com.google.common.base.Equivalence;
import com.google.common.base.Functions;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.Ordering;
import com.google.common.primitives.UnsignedInteger;
import com.google.common.primitives.UnsignedLong;
import com.google.common.testing.ForwardingWrapperTester;
import com.google.common.testing.NullPointerTester;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Pattern;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests for {@link ForwardingWrapperTester}. Live in a different package to detect reflection
 * access issues, if any.
 *
 * @author Ben Yu
 */
class ForwardingWrapperTesterTest {

    private final ForwardingWrapperTester tester = new ForwardingWrapperTester();

    @Test
    void testGoodForwarder() {
        tester.testForwarding(
                Arithmetic.class,
                ForwardingArithmetic::new);
        tester.testForwarding(
                ParameterTypesDifferent.class,
                ParameterTypesDifferentForwarder::new);
    }

    @Test
    void testVoidMethodForwarding() {
        tester.testForwarding(
                Runnable.class,
                ForwardingRunnable::new);
    }

    @Test
    void testToStringForwarding() {
        tester.testForwarding(
                Runnable.class,
                runnable -> new ForwardingRunnable(runnable) {
                    @Override
                    public String toString() {
                        return runnable.toString();
                    }
                });
    }

    @Test
    void testFailsToForwardToString() {
        assertFailure(
                Runnable.class,
                runnable -> new ForwardingRunnable(runnable) {
                    @Override
                    public String toString() {
                        return "";
                    }
                },
                "toString()");
    }

    @Test
    void testFailsToForwardHashCode() {
        tester.includingEquals();
        assertFailure(
                Runnable.class,
                runnable -> new ForwardingRunnable(runnable) {

                    @SuppressWarnings("EqualsHashCode")
                    @Override
                    public boolean equals(Object o) {
                        if (o instanceof ForwardingRunnable) {
                            ForwardingRunnable that = (ForwardingRunnable) o;
                            return runnable.equals(that.runnable);
                        }
                        return false;
                    }
                },
                "Runnable");
    }

    @Test
    void testEqualsAndHashCodeForwarded() {
        tester.includingEquals();
        tester.testForwarding(
                Runnable.class,
                runnable -> new ForwardingRunnable(runnable) {
                    @Override
                    public boolean equals(Object o) {
                        if (o instanceof ForwardingRunnable) {
                            ForwardingRunnable that = (ForwardingRunnable) o;
                            return runnable.equals(that.runnable);
                        }
                        return false;
                    }

                    @Override
                    public int hashCode() {
                        return runnable.hashCode();
                    }
                });
    }

    @Test
    void testFailsToForwardEquals() {
        tester.includingEquals();
        assertFailure(
                Runnable.class,
                runnable -> new ForwardingRunnable(runnable) {
                    @Override
                    public int hashCode() {
                        return runnable.hashCode();
                    }
                },
                "Runnable");
    }

    @Test
    void testFailsToForward() {
        assertFailure(
                Runnable.class,
                runnable -> new ForwardingRunnable(runnable) {
                    @Override
                    public void run() {
                    }
                },
                "run()",
                "Failed to forward");
    }

    @Test
    void testRedundantForwarding() {
        assertFailure(
                Runnable.class,
                runnable -> () -> {
                    runnable.run();
                    runnable.run();
                },
                "run()",
                "invoked more than once");
    }

    @Test
    void testFailsToForwardParameters() {
        assertFailure(
                Adder.class,
                FailsToForwardParameters::new,
                "add(",
                "Parameter #0");
    }

    @Test
    void testForwardsToTheWrongMethod() {
        assertFailure(
                Arithmetic.class,
                ForwardsToTheWrongMethod::new,
                "minus");
    }

    @Test
    void testFailsToForwardReturnValue() {
        assertFailure(
                Adder.class,
                FailsToForwardReturnValue::new,
                "add(",
                "Return value");
    }

    @Test
    void testFailsToPropagateException() {
        assertFailure(
                Adder.class,
                FailsToPropagageException::new,
                "add(",
                "exception");
    }

    @Test
    void testNotInterfaceType() {
        try {
            new ForwardingWrapperTester().testForwarding(String.class, Functions.<String>identity());
            fail();
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test
    void testNulls() {
        new NullPointerTester()
                .setDefault(Class.class, Runnable.class)
                .testAllPublicInstanceMethods(new ForwardingWrapperTester());
    }

    private <T> void assertFailure(
            Class<T> interfaceType,
            Function<T, ? extends T> wrapperFunction,
            String... expectedMessages) {
        try {
            tester.testForwarding(interfaceType, wrapperFunction);
        } catch (AssertionError expected) {
            for (String message : expectedMessages) {
                assertThat(expected.getMessage()).contains(message);
            }
            return;
        }
        fail("expected failure not reported");
    }

    private static class ForwardingRunnable implements Runnable {

        private final Runnable runnable;

        ForwardingRunnable(Runnable runnable) {
            this.runnable = runnable;
        }

        @Override
        public void run() {
            runnable.run();
        }

        @Override
        public String toString() {
            return runnable.toString();
        }
    }

    private interface Adder {
        int add(int a, int b);
    }

    private static class ForwardingArithmetic implements Arithmetic {
        private final Arithmetic arithmetic;

        public ForwardingArithmetic(Arithmetic arithmetic) {
            this.arithmetic = arithmetic;
        }

        @Override
        public int add(int a, int b) {
            return arithmetic.add(a, b);
        }

        @Override
        public int minus(int a, int b) {
            return arithmetic.minus(a, b);
        }

        @Override
        public String toString() {
            return arithmetic.toString();
        }
    }

    private static class FailsToForwardParameters implements Adder {
        private final Adder adder;

        FailsToForwardParameters(Adder adder) {
            this.adder = adder;
        }

        @Override
        public int add(int a, int b) {
            return adder.add(b, a);
        }

        @Override
        public String toString() {
            return adder.toString();
        }
    }

    private static class FailsToForwardReturnValue implements Adder {
        private final Adder adder;

        FailsToForwardReturnValue(Adder adder) {
            this.adder = adder;
        }

        @Override
        public int add(int a, int b) {
            return adder.add(a, b) + 1;
        }

        @Override
        public String toString() {
            return adder.toString();
        }
    }

    private static class FailsToPropagageException implements Adder {
        private final Adder adder;

        FailsToPropagageException(Adder adder) {
            this.adder = adder;
        }

        @Override
        public int add(int a, int b) {
            try {
                return adder.add(a, b);
            } catch (Exception e) {
                // swallow!
                return 0;
            }
        }

        @Override
        public String toString() {
            return adder.toString();
        }
    }

    public interface Arithmetic extends Adder {
        int minus(int a, int b);
    }

    private static class ForwardsToTheWrongMethod implements Arithmetic {
        private final Arithmetic arithmetic;

        ForwardsToTheWrongMethod(Arithmetic arithmetic) {
            this.arithmetic = arithmetic;
        }

        @Override
        public int minus(int a, int b) { // bad!
            return arithmetic.add(a, b);
        }

        @Override
        public int add(int a, int b) {
            return arithmetic.add(a, b);
        }

        @Override
        public String toString() {
            return arithmetic.toString();
        }
    }

    private interface ParameterTypesDifferent {
        void foo(
                String s,
                Runnable r,
                Number n,
                Iterable<?> it,
                boolean b,
                Equivalence<String> eq,
                Exception e,
                InputStream in,
                Comparable<?> c,
                Ordering<Integer> ord,
                Charset charset,
                TimeUnit unit,
                Class<?> cls,
                Joiner joiner,
                Pattern pattern,
                UnsignedInteger ui,
                UnsignedLong ul,
                StringBuilder sb,
                Predicate<?> pred,
                Function<?, ?> func,
                Object obj);
    }

    private static class ParameterTypesDifferentForwarder implements ParameterTypesDifferent {
        private final ParameterTypesDifferent delegate;

        public ParameterTypesDifferentForwarder(ParameterTypesDifferent delegate) {
            this.delegate = delegate;
        }

        @Override
        public void foo(
                String s,
                Runnable r,
                Number n,
                Iterable<?> it,
                boolean b,
                Equivalence<String> eq,
                Exception e,
                InputStream in,
                Comparable<?> c,
                Ordering<Integer> ord,
                Charset charset,
                TimeUnit unit,
                Class<?> cls,
                Joiner joiner,
                Pattern pattern,
                UnsignedInteger ui,
                UnsignedLong ul,
                StringBuilder sb,
                Predicate<?> pred,
                Function<?, ?> func,
                Object obj) {
            delegate.foo(
                    s, r, n, it, b, eq, e, in, c, ord, charset, unit, cls, joiner, pattern, ui, ul, sb, pred,
                    func, obj);
        }

        @Override
        public String toString() {
            return delegate.toString();
        }
    }

    @Test
    void testCovariantReturn() {
        new ForwardingWrapperTester()
                .testForwarding(
                        Sub.class,
                        ForwardingSub::new);
    }

    interface Base {
        CharSequence getId();
    }

    interface Sub extends Base {
        @Override
        String getId();
    }

    private static class ForwardingSub implements Sub {
        private final Sub delegate;

        ForwardingSub(Sub delegate) {
            this.delegate = delegate;
        }

        @Override
        public String getId() {
            return delegate.getId();
        }

        @Override
        public String toString() {
            return delegate.toString();
        }
    }

    private interface Equals {
        @Override
        boolean equals(Object obj);

        @Override
        int hashCode();

        @Override
        String toString();
    }

    private static class NoDelegateToEquals implements Equals {

        private static Function<Equals, Equals> WRAPPER =
                NoDelegateToEquals::new;

        private final Equals delegate;

        NoDelegateToEquals(Equals delegate) {
            this.delegate = delegate;
        }

        @Override
        public String toString() {
            return delegate.toString();
        }
    }

    @Test
    void testExplicitEqualsAndHashCodeNotDelegatedByDefault() {
        new ForwardingWrapperTester().testForwarding(Equals.class, NoDelegateToEquals.WRAPPER);
    }

    @Test
    void testExplicitEqualsAndHashCodeDelegatedWhenExplicitlyAsked() {
        try {
            new ForwardingWrapperTester()
                    .includingEquals()
                    .testForwarding(Equals.class, NoDelegateToEquals.WRAPPER);
        } catch (AssertionError expected) {
            return;
        }
        fail("Should have failed");
    }

    /** An interface for the 2 ways that a chaining call might be defined. */
    private interface ChainingCalls {
        // A method that is defined to 'return this'
        ChainingCalls chainingCall();

        // A method that just happens to return a ChainingCalls object
        ChainingCalls nonChainingCall();
    }

    private static class ForwardingChainingCalls implements ChainingCalls {
        final ChainingCalls delegate;

        ForwardingChainingCalls(ChainingCalls delegate) {
            this.delegate = delegate;
        }

        @Override
        public ForwardingChainingCalls chainingCall() {
            delegate.chainingCall();
            return this;
        }

        @Override
        public ChainingCalls nonChainingCall() {
            return delegate.nonChainingCall();
        }

        @Override
        public String toString() {
            return delegate.toString();
        }
    }

    @Test
    void testChainingCalls() {
        tester.testForwarding(
                ChainingCalls.class,
                ForwardingChainingCalls::new);
    }
}
