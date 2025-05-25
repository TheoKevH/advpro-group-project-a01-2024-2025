//package id.ac.ui.cs.advprog.buildingstore.product.controller;
//
//import id.ac.ui.cs.advprog.buildingstore.product.model.Product;
//import id.ac.ui.cs.advprog.buildingstore.product.service.ProductService;
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
//import java.util.List;
//
//@ExtendWith(MockitoExtension.class)
//public class ProductControllerTest {
//
//    @Mock
//    private ProductService service;
//    @Mock
//    private Model model;
//    @InjectMocks
//    ProductController productController;
//    @BeforeEach
//    void setUp() {
//    }
//
//    @Test
//    public void testCreateProductPage() {
//        String viewName = productController.createProductPage(model);
//        verify(model).addAttribute(eq("product"), any(Product.class));
//        assertEquals("product/createProduct", viewName);
//    }
//
//    @Test
//    public void testCreateProductPost() {
//        Product product = new Product();
//        String viewName = productController.createProductPost(product, model);
//        verify(service).create(product);
//        assertEquals("redirect:list", viewName);
//    }
//
//    @Test
//    public void testProductListPage() {
//        List<Product> productList = Arrays.asList(new Product(), new Product());
//        when(service.findAll()).thenReturn(productList);
//
//        String viewName = productController.productListPage(model);
//        verify(model).addAttribute("products", productList);
//        assertEquals("product/productList", viewName);
//    }
//
//    @Test
//    public void testEditProductPage() {
//        Product product = new Product();
//        when(service.findById("1")).thenReturn(product);
//
//        String viewName = productController.editProductPage("1", model);
//        verify(model).addAttribute("product", product);
//        assertEquals("product/editProduct", viewName);
//    }
//
//    @Test
//    public void testEditProductPost() {
//        Product product = new Product();
//        String viewName = productController.editProductPost(product);
//        verify(service).edit(product);
//        assertEquals("redirect:/product/list", viewName);
//    }
//
//    @Test
//    public void testDeleteProduct() {
//        Product product = new Product();
//        when(service.findById("1")).thenReturn(product);
//
//        String viewName = productController.deleteProduct("1");
//        verify(service).delete(product);
//        assertEquals("redirect:/product/list", viewName);
//    }
//}
