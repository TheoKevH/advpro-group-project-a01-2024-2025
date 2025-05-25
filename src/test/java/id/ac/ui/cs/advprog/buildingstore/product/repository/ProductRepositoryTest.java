//package id.ac.ui.cs.advprog.buildingstore.product.repository;
//
//import id.ac.ui.cs.advprog.buildingstore.product.model.Product;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Iterator;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(MockitoExtension.class)
//class ProductRepositoryTest {
//
//    Product product1;
//    Product product2;
//
//    @InjectMocks
//    ProductRepository productRepository;
//    @BeforeEach
//    void setUp() {
//        this.product1 = new Product();
//        this.product1.setProductId("c0c447ab-c221-48df-a8f6-a2c067411ed7");
//        this.product1.setProductName("palu");
//        this.product1.setProductDescription("palu karet keramik");
//        this.product1.setProductQuantity(50);
//
//        this.product2 = new Product();
//        this.product2.setProductName("obeng");
//        this.product2.setProductDescription("obeng magnet");
//        this.product2.setProductQuantity(25);
//    }
//
//    @Test
//    void testCreateAndFind() {
//        productRepository.create(product1);
//
//        Iterator<Product> productIterator = productRepository.findAll();
//        assertTrue(productIterator.hasNext());
//        Product savedProduct = productIterator.next();
//        assertEquals(product1.getProductId(), savedProduct.getProductId());
//        assertEquals(product1.getProductName(), savedProduct.getProductName());
//        assertEquals(product1.getProductQuantity(), savedProduct.getProductQuantity());
//    }
//
//    @Test
//    void testCreateWithoutId() {
//        productRepository.create(product2);
//
//        assertNotNull(product2.getProductId());
//    }
//
//    @Test
//    void testFindAllifEmpty() {
//        Iterator<Product> productIterator = productRepository.findAll();
//        assertFalse(productIterator.hasNext());
//    }
//
//    @Test
//    void testFindAllifMoreThanOneProduct() {
//        productRepository.create(product1);
//        productRepository.create(product2);
//
//        Iterator<Product> productIterator = productRepository.findAll();
//        assertTrue(productIterator.hasNext());
//        Product savedProduct = productIterator.next();
//        assertEquals(product1.getProductId(), savedProduct.getProductId());
//        savedProduct = productIterator.next();
//        assertEquals(product2.getProductName(), savedProduct.getProductName());
//    }
//
//    @Test
//    void testEdit() {
//        productRepository.create(product1);
//
//        product2.setProductId("c0c447ab-c221-48df-a8f6-a2c067411ed7");
//        productRepository.edit(product2);
//
//        Product editedProduct = productRepository.findById("c0c447ab-c221-48df-a8f6-a2c067411ed7");
//        assertNotEquals("palu", editedProduct.getProductName());
//        assertEquals("obeng", editedProduct.getProductName());
//    }
//
//    @Test
//    void testEditNotFound() {
//        assertThrows(NullPointerException.class, () -> productRepository.edit(product1));
//    }
//
//    @Test
//    void testDelete() {
//        productRepository.create(product1);
//
//        productRepository.delete(product1);
//        assertThrows(NullPointerException.class, () -> productRepository.findById(product1.getProductId()));
//    }
//
//    @Test
//    void testDeleteNotFound() {
//        assertThrows(NullPointerException.class, () -> productRepository.delete(product1));
//    }
//}
