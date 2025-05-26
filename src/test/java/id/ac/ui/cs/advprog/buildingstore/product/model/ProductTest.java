package id.ac.ui.cs.advprog.buildingstore.product.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {
    @Test
    void prePersist_shouldGenerateIdIfNotSet() {
        Product product = Product.builder()
                .productName("Kayu")
                .productPrice(new BigDecimal("10000"))
                .productQuantity(5)
                .build();

        product.ensureId();

        assertThat(product.getProductId()).isNotNull();
        assertThat(product.getProductId()).isNotBlank();
    }
}