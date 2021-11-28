package com.google.common.testing.junit;

/**
 * A <em>Protectable</em> can be run and can throw a Throwable.
 *
 * @see TestResult
 */
public interface Protectable {

    /**
     * Run the following method protected.
     */
    void protect() throws Throwable;
}
