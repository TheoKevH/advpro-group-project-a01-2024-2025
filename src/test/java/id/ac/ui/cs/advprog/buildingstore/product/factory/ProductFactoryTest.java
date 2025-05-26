package id.ac.ui.cs.advprog.buildingstore.product.factory;

import id.ac.ui.cs.advprog.buildingstore.product.dto.ProductDTO;
import id.ac.ui.cs.advprog.buildingstore.product.model.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ProductFactoryTest {

    @Test
    void fromDTO_shouldConvertDTOtoProduct() {
        ProductDTO dto = new ProductDTO(
                "123",
                "Produk A",
                new BigDecimal("10000"),
                10,
                "Deskripsi produk"
        );

        Product product = ProductFactory.fromDTO(dto);

        assertThat(product.getProductId()).isEqualTo("123");
        assertThat(product.getProductName()).isEqualTo("Produk A");
        assertThat(product.getProductPrice()).isEqualByComparingTo("10000");
        assertThat(product.getProductQuantity()).isEqualTo(10);
        assertThat(product.getProductDescription()).isEqualTo("Deskripsi produk");
    }

    @Test
    void fromDTO_shouldThrowExceptionIfDtoIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ProductFactory.fromDTO(null);
        });

        assertThat(exception).hasMessage("ProductDTO must not be null");
    }

    @Test
    void toDTO_shouldConvertProductToDTO() {
        Product product = Product.builder()
                .productId("456")
                .productName("Produk B")
                .productPrice(new BigDecimal("20000"))
                .productQuantity(5)
                .productDescription("Deskripsi produk B")
                .build();

        ProductDTO dto = ProductFactory.toDTO(product);

        assertThat(dto).isNotNull();
        assertThat(dto.getProductId()).isEqualTo("456");
        assertThat(dto.getProductName()).isEqualTo("Produk B");
        assertThat(dto.getProductPrice()).isEqualByComparingTo("20000");
        assertThat(dto.getProductQuantity()).isEqualTo(5);
        assertThat(dto.getProductDescription()).isEqualTo("Deskripsi produk B");
    }

    @Test
    void toDTO_shouldReturnNullIfProductIsNull() {
        ProductDTO dto = ProductFactory.toDTO(null);
        assertThat(dto).isNull();
    }
}
