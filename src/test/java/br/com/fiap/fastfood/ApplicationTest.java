package br.com.fiap.fastfood;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ApplicationTest {

    @Test
    void applicationHasSpringBootApplicationAnnotation() {
        assertTrue(Application.class.isAnnotationPresent(SpringBootApplication.class),
                "Application class should be annotated with @SpringBootApplication");
    }

}

