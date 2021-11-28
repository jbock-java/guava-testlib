package com.google.common.testing.junit;

/**
 * A Listener for test progress
 */
public interface TestListener {
    /**
     * An error occurred.
     */
    void addError(Test test, Throwable e);

    /**
     * A failure occurred.
     */
    void addFailure(Test test, AssertionError e);

    /**
     * A test ended.
     */
    void endTest(Test test);

    /**
     * A test started.
     */
    void startTest(Test test);
}