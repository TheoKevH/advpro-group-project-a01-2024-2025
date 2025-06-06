package id.ac.ui.cs.advprog.buildingstore.transaksi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.buildingstore.authentication.model.User;
import id.ac.ui.cs.advprog.buildingstore.authentication.repository.UserRepository;
import id.ac.ui.cs.advprog.buildingstore.config.TestSecurityConfig;
import id.ac.ui.cs.advprog.buildingstore.transaksi.dto.CreateTransactionRequest;
import id.ac.ui.cs.advprog.buildingstore.transaksi.dto.CustomerDTO;
import id.ac.ui.cs.advprog.buildingstore.transaksi.dto.ProductDTO;
import id.ac.ui.cs.advprog.buildingstore.transaksi.dto.UpdateTransactionRequest;
import id.ac.ui.cs.advprog.buildingstore.transaksi.model.Transaction;
import id.ac.ui.cs.advprog.buildingstore.transaksi.model.TransactionItem;
import id.ac.ui.cs.advprog.buildingstore.transaksi.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
@Import(TestSecurityConfig.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService service;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private Transaction dummy;
    private String dummyId;
    private CreateTransactionRequest createRequest;

    @BeforeEach
    void setup() {
        dummyId = UUID.randomUUID().toString();

        TransactionItem item = new TransactionItem();
        item.setProductId("prod-1");
        item.setQuantity(2);

        dummy = Transaction.builder()
                .transactionId(dummyId)
                .customerId("cust-001")
                .items(List.of(item))
                .build();

        TransactionItem requestItem = new TransactionItem();
        requestItem.setProductId("prod-1");
        requestItem.setQuantity(2);

        createRequest = new CreateTransactionRequest();
        createRequest.setCustomerId("cust-001");
        createRequest.setItems(List.of(requestItem));
    }


    @Test
    void testCreateTransaction_shouldReturnNewTransaction() throws Exception {
        when(service.createTransaction("cust-001", createRequest.getItems())).thenReturn(dummy);

        mockMvc.perform(post("/api/transactions")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value(dummyId))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void testGetAllTransactions_shouldReturnList() throws Exception {
        Transaction trx1 = Transaction.builder().transactionId(UUID.randomUUID().toString()).build();
        Transaction trx2 = Transaction.builder().transactionId(UUID.randomUUID().toString()).build();
        when(service.getAllTransactions()).thenReturn(List.of(trx1, trx2));

        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetTransactionById_shouldReturnTransaction() throws Exception {
        when(service.getTransaction(dummyId)).thenReturn(dummy);

        mockMvc.perform(get("/api/transactions/" + dummyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value(dummyId));
    }

    @Test
    void testMoveToPayment_shouldUpdateState() throws Exception {
        dummy.moveToPayment();
        when(service.moveToPayment(dummyId)).thenReturn(dummy);

        mockMvc.perform(put("/api/transactions/" + dummyId + "/payment")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("AWAITING_PAYMENT"));
    }

    @Test
    void testMarkAsPaid_shouldUpdateState() throws Exception {
        dummy.moveToPayment();
        dummy.markAsPaid();
        when(service.markAsPaid(dummyId)).thenReturn(dummy);

        mockMvc.perform(put("/api/transactions/" + dummyId + "/pay")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void testCancelTransaction_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/transactions/" + dummyId)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    void testUpdateTransaction_shouldReplaceItems() throws Exception {
        TransactionItem itemA = new TransactionItem();
        itemA.setProductId("prod-1");
        itemA.setQuantity(5);

        TransactionItem itemB = new TransactionItem();
        itemB.setProductId("prod-2");
        itemB.setQuantity(3);

        List<TransactionItem> updatedItems = List.of(itemA, itemB);

        UpdateTransactionRequest updateRequest = new UpdateTransactionRequest();
        updateRequest.setItems(updatedItems);

        dummy.setItems(updatedItems);
        when(service.updateTransaction(dummyId, updatedItems)).thenReturn(dummy);

        mockMvc.perform(put("/api/transactions/" + dummyId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(2))
                .andExpect(jsonPath("$.items[0].productId").value("prod-1"))
                .andExpect(jsonPath("$.items[0].quantity").value(5))
                .andExpect(jsonPath("$.items[1].productId").value("prod-2"))
                .andExpect(jsonPath("$.items[1].quantity").value(3));
    }

    @Test
    void testGetTransactionsByCustomer_shouldReturnFilteredTransactions() throws Exception {
        Transaction trx1 = Transaction.builder()
                .transactionId("trx-1")
                .customerId("cust-321")
                .build();

        Transaction trx2 = Transaction.builder()
                .transactionId("trx-2")
                .customerId("cust-321")
                .build();

        when(service.getTransactionsByCustomer("cust-321")).thenReturn(List.of(trx1, trx2));

        mockMvc.perform(get("/api/transactions/customer/cust-321"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @WithMockUser(username = "kasir01", roles = {"KASIR"})
    void testGetMyTransactions_shouldReturnOnlyUserTransactions() throws Exception {
        String username = "kasir01";

        User user = new User();
        user.setId(1L);
        user.setUsername(username);

        Transaction trx1 = Transaction.builder()
                .transactionId("trx-1")
                .customerId("cust-A")
                .createdBy(user)
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(service.getTransactionsByUser(user)).thenReturn(List.of(trx1));

        mockMvc.perform(get("/api/transactions/my-transactions")
                        .with(csrf())
                        .with(user(username).roles("KASIR")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].transactionId").value("trx-1"));
    }

    @Test
    @WithMockUser(username = "admin01", roles = {"ADMIN"})
    void testGetTransactionsByCreator_shouldReturnFilteredList() throws Exception {
        String kasirUsername = "kasir02";

        User kasir = new User();
        kasir.setId(2L);
        kasir.setUsername(kasirUsername);

        Transaction trx1 = Transaction.builder()
                .transactionId("trx-18")
                .customerId("cust-X")
                .createdBy(kasir)
                .build();

        when(userRepository.findByUsername(kasirUsername)).thenReturn(Optional.of(kasir));
        when(service.getTransactionsByUser(kasir)).thenReturn(List.of(trx1));

        mockMvc.perform(get("/api/transactions/created-by/" + kasirUsername)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].transactionId").value("trx-18"));
    }

    @Test
    void testGetTransactionById_shouldReturn404WhenNotFound() throws Exception {
        when(service.getTransaction("non-existent")).thenReturn(null);

        mockMvc.perform(get("/api/transactions/non-existent"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testMoveToPayment_shouldReturnBadRequestOnError() throws Exception {
        when(service.moveToPayment(dummyId)).thenThrow(new IllegalStateException("Invalid state"));

        mockMvc.perform(put("/api/transactions/" + dummyId + "/payment").with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid state"));
    }

    @Test
    void testMarkAsPaid_shouldReturnBadRequestOnError() throws Exception {
        when(service.markAsPaid(dummyId)).thenThrow(new IllegalStateException("Not ready"));

        mockMvc.perform(put("/api/transactions/" + dummyId + "/pay").with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Not ready"));
    }

    @Test
    void testUpdateTransaction_shouldReturnBadRequestOnError() throws Exception {
        UpdateTransactionRequest req = new UpdateTransactionRequest();
        TransactionItem item1 = new TransactionItem();
        item1.setProductId("p1");
        item1.setQuantity(1);;
        req.setItems(List.of(item1));

        when(service.updateTransaction(dummyId, req.getItems()))
                .thenThrow(new IllegalStateException("Not editable"));

        mockMvc.perform(put("/api/transactions/" + dummyId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Not editable"));
    }

    @Test
    void testCancelTransaction_shouldReturnBadRequestOnError() throws Exception {
        doThrow(new IllegalStateException("Already paid"))
                .when(service).cancelTransaction(dummyId);

        mockMvc.perform(delete("/api/transactions/" + dummyId).with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Already paid"));
    }

    @Test
    void testGetMyTransactions_shouldReturnUnauthorizedIfUserNotFound() throws Exception {
        String username = "ghost";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/transactions/my-transactions")
                        .with(csrf())
                        .with(user(username)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetTransactionsByCreator_shouldReturnUnauthorizedIfUserNotFound() throws Exception {
        String targetUsername = "ghost-kasir";
        when(userRepository.findByUsername(targetUsername)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/transactions/created-by/" + targetUsername)
                        .with(csrf())
                        .with(user("admin01").roles("ADMIN")))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetTransactionView_shouldReturnAggregatedData() throws Exception {
        // Mock transaction
        when(service.getTransaction(dummyId)).thenReturn(dummy);

        // Mock product response
        ProductDTO product = new ProductDTO();
        product.setProductId("prod-1");
        product.setProductName("Produk A");
        product.setProductPrice(10000);
        when(restTemplate.getForObject("http://localhost/api/product", ProductDTO[].class))
                .thenReturn(new ProductDTO[]{ product });

        // Mock customer response
        CustomerDTO customer = new CustomerDTO();
        customer.setId("cust-001");
        customer.setName("John Doe");
        when(restTemplate.getForObject("http://localhost/api/customers/cust-001", CustomerDTO.class))
                .thenReturn(customer);

        mockMvc.perform(get("/api/transactions/" + dummyId + "/view"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("John Doe"))
                .andExpect(jsonPath("$.items.length()").value(1))
                .andExpect(jsonPath("$.items[0].productName").value("Produk A"))
                .andExpect(jsonPath("$.items[0].productPrice").value(10000))
                .andExpect(jsonPath("$.items[0].quantity").value(2));
    }

    @Test
    void testGetTransactionView_shouldHandleNullCustomer() throws Exception {
        when(service.getTransaction(dummyId)).thenReturn(dummy);

        ProductDTO product = new ProductDTO();
        product.setProductId("prod-1");
        product.setProductName("Produk A");
        product.setProductPrice(10000);

        when(restTemplate.getForObject("http://localhost/api/product", ProductDTO[].class))
                .thenReturn(new ProductDTO[]{ product });

        when(restTemplate.getForObject("http://localhost/api/customers/cust-001", CustomerDTO.class))
                .thenReturn(null); // simulate not found

        mockMvc.perform(get("/api/transactions/" + dummyId + "/view"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("Unknown"))
                .andExpect(jsonPath("$.items[0].productName").value("Produk A"));
    }

    @Test
    void testGetTransactionView_shouldHandleUnknownProduct() throws Exception {
        when(service.getTransaction(dummyId)).thenReturn(dummy);

        // Empty product list → no matching product in map
        when(restTemplate.getForObject("http://localhost/api/product", ProductDTO[].class))
                .thenReturn(new ProductDTO[0]);

        CustomerDTO customer = new CustomerDTO();
        customer.setId("cust-001");
        customer.setName("John Doe");

        when(restTemplate.getForObject("http://localhost/api/customers/cust-001", CustomerDTO.class))
                .thenReturn(customer);

        mockMvc.perform(get("/api/transactions/" + dummyId + "/view"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("John Doe"))
                .andExpect(jsonPath("$.items[0].productName").value("Unknown"))
                .andExpect(jsonPath("$.items[0].productPrice").value(0));
    }


}
