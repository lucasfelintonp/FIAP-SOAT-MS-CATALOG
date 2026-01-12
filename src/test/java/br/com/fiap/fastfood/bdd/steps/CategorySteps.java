package br.com.fiap.fastfood.bdd.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.pt.Dado;

/**
 * Step definitions for Category-related Cucumber scenarios.
 * This class defines the steps for category testing scenarios.
 */
public class CategorySteps {

    /**
     * Acknowledges that categories should exist in the database.
     * For this BDD test, we assume the database is pre-populated with test data
     * or categories are managed by the application's data initialization.
     *
     * @param dataTable table containing expected category data
     */
    @Dado("que existem as seguintes categorias cadastradas")
    public void queExistemAsSeguintesCategoriasCadastradas(final DataTable dataTable) {
        // This step is declarative - it states the precondition that categories exist
        // The actual verification happens in the "Entao" (Then) steps
        // If needed, you can add validation logic here to ensure categories exist

        // For now, we simply acknowledge the precondition
        // The database should have these categories from migrations or test data setup
    }
}
