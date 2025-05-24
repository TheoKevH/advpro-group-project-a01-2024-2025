package id.ac.ui.cs.advprog.buildingstore.transaksi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.buildingstore.config.TestSecurityConfig;
import id.ac.ui.cs.advprog.buildingstore.transaksi.model.Transaction;
import id.ac.ui.cs.advprog.buildingstore.transaksi.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
@Import(TestSecurityConfig.class)
class SupplierTransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateTransaction_shouldReturnNewTransaction() throws Exception {
        Transaction dummy = new Transaction();
        when(service.createTransaction()).thenReturn(dummy);

        mockMvc.perform(post("/api/transactions")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dummy.getId()))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void testGetAllTransactions_shouldReturnList() throws Exception {
        Transaction dummy1 = new Transaction();
        Transaction dummy2 = new Transaction();
        when(service.getAllTransactions()).thenReturn(List.of(dummy1, dummy2));

        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetTransactionById_shouldReturnTransaction() throws Exception {
        Transaction dummy = new Transaction();
        when(service.getTransaction(dummy.getId())).thenReturn(dummy);

        mockMvc.perform(get("/api/transactions/" + dummy.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dummy.getId()));
    }

    @Test
    void testMoveToPayment_shouldUpdateState() throws Exception {
        Transaction dummy = new Transaction();
        dummy.moveToPayment(); // state berubah ke AWAITING_PAYMENT
        when(service.moveToPayment(dummy.getId())).thenReturn(dummy);

        mockMvc.perform(put("/api/transactions/" + dummy.getId() + "/payment")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("AWAITING_PAYMENT"));
    }

    @Test
    void testMarkAsPaid_shouldUpdateState() throws Exception {
        Transaction dummy = new Transaction();
        dummy.moveToPayment();
        dummy.markAsPaid(); // state berubah ke COMPLETED
        when(service.markAsPaid(dummy.getId())).thenReturn(dummy);

        mockMvc.perform(put("/api/transactions/" + dummy.getId() + "/pay")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void testCancelTransaction_shouldReturnNoContent() throws Exception {
        Transaction dummy = new Transaction();
        mockMvc.perform(delete("/api/transactions/" + dummy.getId())
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
