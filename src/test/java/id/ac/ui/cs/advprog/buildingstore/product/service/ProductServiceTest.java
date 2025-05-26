package id.ac.ui.cs.advprog.buildingstore.product.service;

import id.ac.ui.cs.advprog.buildingstore.product.dto.ProductDTO;
import id.ac.ui.cs.advprog.buildingstore.product.dto.ProductRequestDTO;
import id.ac.ui.cs.advprog.buildingstore.product.factory.ProductFactory;
import id.ac.ui.cs.advprog.buildingstore.product.model.Product;
import id.ac.ui.cs.advprog.buildingstore.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplementTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImplement productService;

    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        sampleProduct = Product.builder()
                .productId("1")
                .productName("Kayu")
                .productDescription("Kayu kuat")
                .productPrice(BigDecimal.valueOf(10000))
                .productQuantity(10)
                .build();
    }

    @Test
    void create_shouldSaveNewProduct() {
        ProductDTO dto = ProductFactory.toDTO(sampleProduct);
        when(productRepository.findByProductNameIgnoreCase("Kayu")).thenReturn(Optional.empty());
        when(productRepository.save(any())).thenReturn(sampleProduct);

        Product created = productService.create(dto);

        assertThat(created.getProductName()).isEqualTo("Kayu");
        verify(productRepository).save(any());
    }

    @Test
    void create_shouldThrowIfProductNameExists() {
        ProductDTO dto = ProductFactory.toDTO(sampleProduct);
        when(productRepository.findByProductNameIgnoreCase("Kayu")).thenReturn(Optional.of(sampleProduct));

        assertThatThrownBy(() -> productService.create(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("sudah ada");
    }

    @Test
    void findAll_shouldReturnProductList() {
        when(productRepository.findAll()).thenReturn(List.of(sampleProduct));

        List<Product> products = productService.findAll();

        assertThat(products).hasSize(1);
    }

    @Test
    void findById_shouldReturnProduct() {
        when(productRepository.findById("1")).thenReturn(Optional.of(sampleProduct));

        Product found = productService.findById("1");

        assertThat(found.getProductName()).isEqualTo("Kayu");
    }

    @Test
    void findById_shouldThrowIfNotFound() {
        when(productRepository.findById("99")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.findById("99"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("tidak ditemukan");
    }

    @Test
    void edit_shouldUpdateProduct() {
        ProductDTO dto = ProductFactory.toDTO(sampleProduct);
        dto.setProductQuantity(20);

        when(productRepository.findById("1")).thenReturn(Optional.of(sampleProduct));
        when(productRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Product updated = productService.edit(dto);

        assertThat(updated.getProductQuantity()).isEqualTo(20);
    }

    @Test
    void delete_shouldRemoveProduct() {
        when(productRepository.existsById("1")).thenReturn(true);

        productService.delete("1");

        verify(productRepository).deleteById("1");
    }

    @Test
    void delete_shouldThrowIfNotFound() {
        when(productRepository.existsById("404")).thenReturn(false);

        assertThatThrownBy(() -> productService.delete("404"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("tidak ditemukan");
    }

    @Test
    void insert_shouldAddQuantityIfProductExists() {
        ProductRequestDTO request = new ProductRequestDTO();
        request.setProductName("Kayu");
        request.setProductQuantity(5);

        when(productRepository.findByProductNameIgnoreCase("Kayu")).thenReturn(Optional.of(sampleProduct));
        when(productRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Product result = productService.insert(request);

        assertThat(result.getProductQuantity()).isEqualTo(15);
    }

    @Test
    void insert_shouldCreateNewProductIfNotExists() {
        ProductRequestDTO request = new ProductRequestDTO();
        request.setProductName("Besi");
        request.setProductQuantity(10);

        when(productRepository.findByProductNameIgnoreCase("Besi")).thenReturn(Optional.empty());
        when(productRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Product result = productService.insert(request);

        assertThat(result.getProductName()).isEqualTo("Besi");
        assertThat(result.getProductQuantity()).isEqualTo(10);
        assertThat(result.getProductPrice()).isEqualTo(BigDecimal.ZERO);
    }
}