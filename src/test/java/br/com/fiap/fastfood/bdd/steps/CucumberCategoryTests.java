package br.com.fiap.fastfood.bdd.steps;

import br.com.fiap.fastfood.category.infrastructure.database.adapters.ProductCategoryAdapter;
import io.cucumber.junit.platform.engine.Constants;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

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

    @MockitoBean
    private ProductCategoryAdapter categoryAdapter;
}
