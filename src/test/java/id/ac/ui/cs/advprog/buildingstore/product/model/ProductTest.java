package id.ac.ui.cs.advprog.buildingstore.product.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTest {

    @Test
    void builder_shouldBuildCorrectProduct() {
        Product product = Product.builder()
                .productId("123")
                .productName("Kayu")
                .productPrice(new BigDecimal("10000"))
                .productQuantity(5)
                .productDescription("Kayu jati")
                .build();

        assertThat(product.getProductId()).isEqualTo("123");
        assertThat(product.getProductName()).isEqualTo("Kayu");
        assertThat(product.getProductPrice()).isEqualByComparingTo("10000");
        assertThat(product.getProductQuantity()).isEqualTo(5);
        assertThat(product.getProductDescription()).isEqualTo("Kayu jati");
    }

    @Test
    void settersAndGetters_shouldWorkCorrectly() {
        Product product = new Product();
        product.setProductId("456");
        product.setProductName("Besi");
        product.setProductPrice(new BigDecimal("20000"));
        product.setProductQuantity(10);
        product.setProductDescription("Besi beton");

        assertThat(product.getProductId()).isEqualTo("456");
        assertThat(product.getProductName()).isEqualTo("Besi");
        assertThat(product.getProductPrice()).isEqualByComparingTo("20000");
        assertThat(product.getProductQuantity()).isEqualTo(10);
        assertThat(product.getProductDescription()).isEqualTo("Besi beton");
    }

    @Test
    void allArgsConstructor_shouldSetAllFields() {
        Product product = new Product(
                "789",
                "Semen",
                new BigDecimal("50000"),
                20,
                "Semen instan"
        );

        assertThat(product.getProductId()).isEqualTo("789");
        assertThat(product.getProductName()).isEqualTo("Semen");
        assertThat(product.getProductPrice()).isEqualByComparingTo("50000");
        assertThat(product.getProductQuantity()).isEqualTo(20);
        assertThat(product.getProductDescription()).isEqualTo("Semen instan");
    }

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

    @Test
    void prePersist_shouldGenerateIdIfBlank() {
        Product product = Product.builder()
                .productId("   ")
                .productName("Kayu")
                .productPrice(new BigDecimal("10000"))
                .productQuantity(5)
                .build();

        product.ensureId();

        assertThat(product.getProductId()).isNotNull();
        assertThat(product.getProductId()).isNotBlank();
    }

    @Test
    void prePersist_shouldNotOverrideExistingId() {
        Product product = Product.builder()
                .productId("existing-id")
                .productName("Baja")
                .productPrice(new BigDecimal("30000"))
                .productQuantity(15)
                .build();

        product.ensureId();

        assertThat(product.getProductId()).isEqualTo("existing-id");
    }
}
