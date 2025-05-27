package id.ac.ui.cs.advprog.buildingstore.transaksi.controller;

import id.ac.ui.cs.advprog.buildingstore.authentication.model.Role;
import id.ac.ui.cs.advprog.buildingstore.authentication.model.User;
import id.ac.ui.cs.advprog.buildingstore.authentication.repository.UserRepository;
import id.ac.ui.cs.advprog.buildingstore.transaksi.dto.CustomerDTO;
import id.ac.ui.cs.advprog.buildingstore.transaksi.dto.CreateTransactionRequest;
import id.ac.ui.cs.advprog.buildingstore.transaksi.dto.ProductDTO;
import id.ac.ui.cs.advprog.buildingstore.transaksi.enums.TransactionStatus;
import id.ac.ui.cs.advprog.buildingstore.transaksi.model.Transaction;
import id.ac.ui.cs.advprog.buildingstore.transaksi.model.TransactionItem;
import id.ac.ui.cs.advprog.buildingstore.transaksi.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionPageController.class)
class TransactionPageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RestTemplate restTemplate;

    private User adminUser;
    private User cashierUser;
    private Transaction transaction;

    @BeforeEach
    void setup() {
        adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setRole(Role.ADMIN);

        cashierUser = new User();
        cashierUser.setUsername("kasir");
        cashierUser.setRole(Role.ADMIN);

        TransactionItem item = new TransactionItem();
        item.setProductId("p001");
        item.setQuantity(2);

        transaction = Transaction.builder()
                .transactionId(UUID.randomUUID().toString())
                .customerId("cust-1")
                .status(TransactionStatus.IN_PROGRESS)
                .items(List.of(item))
                .createdBy(adminUser)
                .build();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCashierTransactions_asAdmin() throws Exception {
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(adminUser));
        when(transactionService.getAllTransactions()).thenReturn(List.of(transaction));
        when(restTemplate.getForObject(contains("/api/customers"), any())).thenReturn(new CustomerDTO[]{
                new CustomerDTO() {{
                    setId("cust-1");
                    setName("Budi");
                    setEmail("budi@email.com");
                    setPhone("08123456789");
                }}
        });

        mockMvc.perform(get("/transaksi"))
                .andExpect(status().isOk())
                .andExpect(view().name("transaksi/listTransaksi"))
                .andExpect(model().attributeExists("transactions"))
                .andExpect(model().attributeExists("customerMap"))
                .andExpect(model().attributeExists("username"));
    }

    @Test
    @WithMockUser(username = "kasir", roles = {"KASIR"})
    void testCashierTransactions_asCashier() throws Exception {
        when(userRepository.findByUsername("kasir")).thenReturn(Optional.of(cashierUser));
        when(transactionService.getTransactionsByUser(cashierUser)).thenReturn(List.of(transaction));
        when(restTemplate.getForObject(contains("/api/customers"), any())).thenReturn(new CustomerDTO[]{
                new CustomerDTO() {{
                    setId("cust-1");
                    setName("Budi");
                    setEmail("budi@email.com");
                    setPhone("08123456789");
                }}
        });

        mockMvc.perform(get("/transaksi"))
                .andExpect(status().isOk())
                .andExpect(view().name("transaksi/listTransaksi"))
                .andExpect(model().attributeExists("transactions"))
                .andExpect(model().attributeExists("customerMap"))
                .andExpect(model().attributeExists("username"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testShowCreateTransactionPage() throws Exception {
        when(restTemplate.getForObject(contains("/api/customers"), any())).thenReturn(new CustomerDTO[]{
                new CustomerDTO() {{
                    setId("cust-1");
                    setName("Budi");
                    setEmail("budi@email.com");
                    setPhone("08123456789");
                }}
        });

        when(restTemplate.getForObject(contains("/api/product"), any())).thenReturn(new ProductDTO[]{
                new ProductDTO() {{
                    setProductId("p001");
                    setProductName("Product 1");
                    setProductDescription("Deskripsi product");
                    setProductQuantity(100);
                    setProductPrice(15000);
                }}
        });

        mockMvc.perform(get("/transaksi/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("transaksi/createNewTransaksi"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attributeExists("customers"))
                .andExpect(model().attributeExists("createRequest"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateTransaction() throws Exception {
        TransactionItem item = new TransactionItem();
        item.setProductId("p001");
        item.setQuantity(1);

        CreateTransactionRequest req = new CreateTransactionRequest();
        req.setCustomerId("cust-1");
        req.setItems(List.of(item));

        doNothing().when(restTemplate).put(anyString(), any());
        when(transactionService.createTransaction(any(), any())).thenReturn(transaction);

        mockMvc.perform(post("/transaksi/create")
                        .with(csrf())
                        .param("customerId", "cust-1")
                        .param("items[0].productId", "p001")
                        .param("items[0].quantity", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/transaksi"));
    }
}
