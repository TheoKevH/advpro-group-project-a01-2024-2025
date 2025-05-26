package id.ac.ui.cs.advprog.buildingstore.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.buildingstore.product.dto.ProductDTO;
import id.ac.ui.cs.advprog.buildingstore.product.dto.ProductRequestDTO;
import id.ac.ui.cs.advprog.buildingstore.product.factory.ProductFactory;
import id.ac.ui.cs.advprog.buildingstore.product.model.Product;
import id.ac.ui.cs.advprog.buildingstore.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductRestControllerTest {

    @InjectMocks
    private ProductRestController controller;

    @Mock
    private ProductService service;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createProduct_shouldReturnCreatedProductDTO() throws Exception {
        ProductDTO dto = new ProductDTO();
        dto.setProductName("Test");
        dto.setProductQuantity(5);
        dto.setProductPrice(BigDecimal.TEN);

        Product product = ProductFactory.fromDTO(dto);

        when(service.create(any())).thenReturn(product);

        mockMvc.perform(post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Test"))
                .andExpect(jsonPath("$.productQuantity").value(5));

        verify(service).create(any());
    }

    @Test
    void getAllProducts_shouldReturnFilteredProducts() throws Exception {
        Product p1 = Product.builder()
                .productName("Prod 1")
                .productPrice(BigDecimal.valueOf(10))
                .build();
        Product p2 = Product.builder()
                .productName("Prod 2")
                .productPrice(BigDecimal.ZERO)  // Should be filtered out
                .build();

        when(service.findAll()).thenReturn(List.of(p1, p2));

        mockMvc.perform(get("/api/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productName").value("Prod 1"))
                .andExpect(jsonPath("$[1]").doesNotExist());

        verify(service).findAll();
    }

    @Test
    void getProductById_shouldReturnProductDTO() throws Exception {
        String id = "abc123";
        Product product = Product.builder()
                .productId(id)
                .productName("ProdById")
                .productPrice(BigDecimal.valueOf(20))
                .productQuantity(3)
                .build();

        when(service.findById(id)).thenReturn(product);

        mockMvc.perform(get("/api/product/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(id))
                .andExpect(jsonPath("$.productName").value("ProdById"));

        verify(service).findById(id);
    }

    @Test
    void updateProductQuantity_shouldUpdateAndReturnProductDTO() throws Exception {
        String id = "123";
        Integer quantity = 2;

        Product existing = Product.builder()
                .productId(id)
                .productQuantity(5)
                .build();

        Product updated = Product.builder()
                .productId(id)
                .productQuantity(3)
                .build();

        when(service.findById(id)).thenReturn(existing);
        when(service.edit(any())).thenReturn(updated);

        mockMvc.perform(put("/api/product/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(quantity)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productQuantity").value(3));

        verify(service).findById(id);
        verify(service).edit(any());
    }

    @Test
    void deleteProduct_shouldReturnOk() throws Exception {
        String id = "123";

        doNothing().when(service).delete(id);

        mockMvc.perform(delete("/api/product/{id}", id))
                .andExpect(status().isOk());

        verify(service).delete(id);
    }

    @Test
    void insertProduct_shouldCallInsert() throws Exception {
        ProductRequestDTO request = new ProductRequestDTO();
        request.setProductName("InsertProd");
        request.setProductQuantity(7);

        mockMvc.perform(post("/api/product/insert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(service).insert(any(ProductRequestDTO.class));
    }

    @Test
    void getAllProducts_shouldFilterOutProductsWithNullOrZeroPrice() throws Exception {
        Product p1 = Product.builder()
                .productName("Valid Product")
                .productPrice(BigDecimal.valueOf(100))
                .build();

        Product p2 = Product.builder()
                .productName("Zero Price Product")
                .productPrice(BigDecimal.ZERO)
                .build();

        Product p3 = Product.builder()
                .productName("Null Price Product")
                .productPrice(null)
                .build();

        when(service.findAll()).thenReturn(List.of(p1, p2, p3));

        mockMvc.perform(get("/api/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productName").value("Valid Product"))
                .andExpect(jsonPath("$[1]").doesNotExist());

        verify(service).findAll();
    }

}
