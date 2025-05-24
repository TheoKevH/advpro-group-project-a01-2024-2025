package id.ac.ui.cs.advprog.buildingstore.transaksi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.buildingstore.config.TestSecurityConfig;
import id.ac.ui.cs.advprog.buildingstore.transaksi.dto.CreateTransactionRequest;
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
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
@Import(TestSecurityConfig.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService service;

    @Autowired
    private ObjectMapper objectMapper;

    private Transaction dummy;
    private String dummyId;
    private CreateTransactionRequest createRequest;

    @BeforeEach
    void setup() {
        dummyId = UUID.randomUUID().toString();

        dummy = Transaction.builder()
                .transactionId(dummyId)
                .customerId("cust-001")
                .items(List.of(new TransactionItem("prod-1", 2)))
                .build();

        createRequest = new CreateTransactionRequest();
        createRequest.setCustomerId("cust-001");
        createRequest.setItems(List.of(new TransactionItem("prod-1", 2)));
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
}
