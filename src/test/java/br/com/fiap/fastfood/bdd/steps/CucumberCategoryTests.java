package br.com.fiap.fastfood.bdd.steps;

import io.cucumber.junit.platform.engine.Constants;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Cucumber test suite for Category features.
 * This class configures the Cucumber test runner with Spring Boot integration.
 * The actual test steps are defined in CategorySteps and RequestsSteps classes.
 */
@Suite
@SelectClasspathResource("features/CategoryTests.feature")
@ConfigurationParameter(
    key = Constants.GLUE_PROPERTY_NAME,
    value = "br.com.fiap.fastfood.bdd.steps"
)
@ConfigurationParameter(key = Constants.PLUGIN_PROPERTY_NAME, value = "pretty")
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CucumberCategoryTests {
    // This class serves as the test suite runner.
    // Test steps are implemented in separate step definition classes.
}
