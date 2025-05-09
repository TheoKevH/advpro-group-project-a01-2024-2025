package id.ac.ui.cs.advprog.buildingstore.product.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {
    Product product;
    @BeforeEach
    void setUp() {
        this.product = new Product();
        this.product.setProductId("c0c447ab-c221-48df-a8f6-a2c067411ed7");
        this.product.setProductName("palu");
        this.product.setProductDescription("palu karet keramik");
        this.product.setProductQuantity(50);
    }
    @Test
    void testGetProductId() {
        assertEquals("c0c447ab-c221-48df-a8f6-a2c067411ed7", this.product.getProductId());
    }

    @Test
    void testGetProductName() {
        assertEquals("palu", this.product.getProductName());
    }

    @Test
    void testGetProductDescription() {
        assertEquals("palu karet keramik", this.product.getProductDescription());
    }

    @Test
    void testGetProductQuantity() {
        assertEquals(50, product.getProductQuantity());
    }

}