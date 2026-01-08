package br.com.fiap.fastfood.bdd.steps;

import br.com.fiap.fastfood.category.application.dtos.ProductCategoryDTO;
import br.com.fiap.fastfood.category.infrastructure.database.adapters.ProductCategoryAdapter;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.pt.Dado;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CategorySteps {

    @Autowired
    private ProductCategoryAdapter categoryAdapter;

    @Dado("que existem as seguintes categorias cadastradas")
    public void queExistemAsSeguintesCategoriasCadastradas(DataTable dataTable) {
        List<ProductCategoryDTO> categorias = dataTable.asMaps().stream()
            .map(map -> new ProductCategoryDTO(
                Integer.valueOf(map.get("id")),
                map.get("name")
            ))
            .toList();

        Mockito.when(categoryAdapter.findAll()).thenReturn(categorias);
    }
}
