package id.ac.ui.cs.advprog.buildingstore.product.controller;

import id.ac.ui.cs.advprog.buildingstore.product.dto.ProductDTO;
import id.ac.ui.cs.advprog.buildingstore.product.factory.ProductFactory;
import id.ac.ui.cs.advprog.buildingstore.product.model.Product;
import id.ac.ui.cs.advprog.buildingstore.product.service.ProductService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductControllerTest {

    @InjectMocks
    private ProductController controller;

    @Mock
    private ProductService service;

    @Mock
    private Model model;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void createProductPage_shouldReturnCreateView() throws Exception {
        mockMvc.perform(get("/product/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("product/createProduct"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    void createProductPost_shouldCallServiceAndRedirect() throws Exception {
        mockMvc.perform(post("/product/create")
                        .param("productName", "Test Product")
                        .param("productQuantity", "5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product"));

        verify(service).create(any());
    }

    @Test
    void productListPage_shouldAddProductsAndReturnView() throws Exception {
        Product p1 = Product.builder().productId("id1").productName("Prod1").productPrice(BigDecimal.TEN).build();
        Product p2 = Product.builder().productId("id2").productName("Prod2").productPrice(BigDecimal.ONE).build();

        when(service.findAll()).thenReturn(List.of(p1, p2));

        mockMvc.perform(get("/product"))
                .andExpect(status().isOk())
                .andExpect(view().name("product/productList"))
                .andExpect(model().attributeExists("products"));

        verify(service).findAll();
    }

    @Test
    void editProductPage_shouldLoadProductAndReturnView() throws Exception {
        String id = "123";
        Product product = Product.builder().productId(id).productName("Test").build();
        when(service.findById(id)).thenReturn(product);

        mockMvc.perform(get("/product/edit/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("product/editProduct"))
                .andExpect(model().attributeExists("product"));

        verify(service).findById(id);
    }

    @Test
    void editProductPost_shouldCallServiceAndRedirect() throws Exception {
        mockMvc.perform(post("/product/edit")
                        .param("productId", "id1")
                        .param("productName", "Updated")
                        .param("productQuantity", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product"));

        verify(service).edit(any());
    }

    @Test
    void deleteProduct_shouldCallServiceAndRedirect() throws Exception {
        String id = "deleteId";

        mockMvc.perform(get("/product/delete/{id}", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product"));

        verify(service).delete(id);
    }
}