package io.castelo.main_server.integration_tests;


import org.junit.jupiter.api.Tag;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;


@Suite
@SelectPackages("io.castelo.main_server.integration_tests.test_classes")
@Tag("integration")
public class IntegrationSuiteTests {
}