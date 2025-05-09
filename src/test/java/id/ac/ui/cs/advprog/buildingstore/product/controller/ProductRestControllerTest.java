package id.ac.ui.cs.advprog.buildingstore.product.controller;

import id.ac.ui.cs.advprog.buildingstore.product.model.Product;
import id.ac.ui.cs.advprog.buildingstore.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductRestControllerTest {

    @Mock
    private ProductService service;

    @InjectMocks
    private ProductRestController productController;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductId("123");
        product.setProductName("Test Product");
        product.setProductQuantity(10);
    }

    @Test
    void testCreateProduct() {
        when(service.create(any(Product.class))).thenReturn(product);

        Product result = productController.createProduct(product);
        assertEquals("123", result.getProductId());
        assertEquals("Test Product", result.getProductName());
        verify(service).create(product);
    }

    @Test
    void testGetAllProducts() {
        List<Product> productList = Arrays.asList(product);
        when(service.findAll()).thenReturn(productList);

        List<Product> result = productController.getAllProducts();
        assertEquals(1, result.size());
        assertEquals("123", result.get(0).getProductId());
        verify(service).findAll();
    }

    @Test
    void testGetProductById() {
        when(service.findById("123")).thenReturn(product);

        Product result = productController.getProductById("123");
        assertNotNull(result);
        assertEquals("Test Product", result.getProductName());
        verify(service).findById("123");
    }

    @Test
    void testUpdateProduct() {
        when(service.edit(any(Product.class))).thenReturn(product);

        Product updated = new Product();
        updated.setProductName("Updated Product");
        updated.setProductQuantity(20);

        Product result = productController.updateProduct("123", updated);
        assertEquals("123", result.getProductId());
        verify(service).edit(updated);
    }

    @Test
    void testDeleteProduct() {
        when(service.findById("123")).thenReturn(product);
        doNothing().when(service).delete(product);

        assertDoesNotThrow(() -> productController.deleteProduct("123"));
        verify(service).findById("123");
        verify(service).delete(product);
    }
}
