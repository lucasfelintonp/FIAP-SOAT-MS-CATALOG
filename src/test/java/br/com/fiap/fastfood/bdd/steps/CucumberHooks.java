package br.com.fiap.fastfood.bdd.steps;

import io.cucumber.java.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

/**
 * Cucumber hooks for setup and teardown operations.
 */
public class CucumberHooks {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Set up test data before each scenario.
     * Ensures categories exist in the test database.
     */
    @Before
    @Transactional
    public void beforeEachScenario() {
        // Insert test categories if they don't exist
        try {
            jdbcTemplate.execute("INSERT INTO product_categories (id, name) VALUES (1, 'Lanche') ON CONFLICT (id) DO NOTHING");
            jdbcTemplate.execute("INSERT INTO product_categories (id, name) VALUES (2, 'Bebida') ON CONFLICT (id) DO NOTHING");
            jdbcTemplate.execute("INSERT INTO product_categories (id, name) VALUES (3, 'Sobremesa') ON CONFLICT (id) DO NOTHING");
            jdbcTemplate.execute("INSERT INTO product_categories (id, name) VALUES (4, 'Acompanhamentos') ON CONFLICT (id) DO NOTHING");
        } catch (Exception e) {
            // Categories might already exist or table might not exist yet
            // Continue with the test
        }
    }
}

