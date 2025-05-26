package id.ac.ui.cs.advprog.buildingstore.product.repository;

import id.ac.ui.cs.advprog.buildingstore.product.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void saveAndFindProduct_shouldWorkCorrectly() {
        Product product = Product.builder()
                .productName("Kayu")
                .productPrice(BigDecimal.valueOf(10000))
                .productQuantity(10)
                .build();

        Product saved = productRepository.save(product);
        Optional<Product> found = productRepository.findById(saved.getProductId());

        assertThat(found).isPresent();
        assertThat(found.get().getProductName()).isEqualTo("Kayu");
    }
}
