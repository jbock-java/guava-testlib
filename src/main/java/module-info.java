module com.google.common.testing {
    requires junit;
    requires com.google.common;
    requires java.logging;

    exports com.google.common.testing;
    exports com.google.common.collect.testing;
    exports com.google.common.collect.testing.features;
    exports com.google.common.collect.testing.google;
    exports com.google.common.collect.testing.testers;
    exports com.google.common.util.concurrent.testing;
    exports com.google.common.escape.testing;
}