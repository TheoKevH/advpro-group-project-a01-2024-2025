//package id.ac.ui.cs.advprog.buildingstore.product.service;
//
//import id.ac.ui.cs.advprog.buildingstore.product.model.Product;
//import id.ac.ui.cs.advprog.buildingstore.product.repository.ProductRepository;
//import id.ac.ui.cs.advprog.buildingstore.product.service.ProductServiceImplement;
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.ui.Model;
//
//import java.util.Arrays;
//import java.util.Iterator;
//import java.util.List;
//
//@ExtendWith(MockitoExtension.class)
//public class ProductServiceTest {
//
//    @Mock
//    private Model model;
//    @Mock
//    private ProductRepository productRepository;
//    @InjectMocks
//    ProductServiceImplement productService;
//    @BeforeEach
//    void setUp() {
//    }
//
//    @Test
//    public void testCreate() {
//        Product product = new Product();
//        when(productRepository.create(product)).thenReturn(product);
//
//        Product createdProduct = productService.create(product);
//        verify(productRepository).create(product);
//        assertEquals(product, createdProduct);
//    }
//
//    @Test
//    public void testFindAll() {
//        Product product1 = new Product();
//        Product product2 = new Product();
//        List<Product> productList = Arrays.asList(product1, product2);
//        Iterator<Product> iterator = productList.iterator();
//
//        when(productRepository.findAll()).thenReturn(iterator);
//
//        List<Product> result = productService.findAll();
//        assertEquals(2, result.size());
//        assertTrue(result.contains(product1));
//        assertTrue(result.contains(product2));
//    }
//
//    @Test
//    public void testFindById() {
//        Product product = new Product();
//        when(productRepository.findById("1")).thenReturn(product);
//
//        Product foundProduct = productService.findById("1");
//        verify(productRepository).findById("1");
//        assertEquals(product, foundProduct);
//    }
//
//    @Test
//    public void testEdit() {
//        Product product = new Product();
//        when(productRepository.edit(product)).thenReturn(product);
//
//        Product editedProduct = productService.edit(product);
//        verify(productRepository).edit(product);
//        assertEquals(product, editedProduct);
//    }
//
//    @Test
//    public void testDelete() {
//        Product product = new Product();
//        doNothing().when(productRepository).delete(product);
//
//        productService.delete(product);
//        verify(productRepository).delete(product);
//    }
//}