package br.com.fiap.fastfood.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");
        localServer.setDescription("Servidor de Desenvolvimento Local");

        return new OpenAPI()
                .info(new Info()
                        .title("FastFood Catalog API")
                        .version("1.0.0")
                        .description("Microserviço de Catálogo para gerenciamento de produtos, categorias e inventário. " +
                                "Esta API permite realizar operações CRUD completas em produtos, categorias e controle de estoque.")
                        .contact(new Contact()
                                .name("FIAP SOAT")
                                .url("https://github.com/FIAP-SOAT"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(localServer));
    }
}

