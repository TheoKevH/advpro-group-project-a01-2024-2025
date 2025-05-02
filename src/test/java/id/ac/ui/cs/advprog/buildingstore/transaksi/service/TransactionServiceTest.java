package id.ac.ui.cs.advprog.buildingstore.transaksi.service;

import id.ac.ui.cs.advprog.buildingstore.transaksi.model.Transaction;
import id.ac.ui.cs.advprog.buildingstore.transaksi.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class TransactionServiceTest {

    @Autowired
    TransactionServiceImpl service;

    @MockBean
    TransactionRepository repository;

    @Test
    void testCreateTransaction_shouldInitializeWithInProgressState() {
        Transaction dummy = new Transaction();
        when(repository.save(any(Transaction.class))).thenReturn(dummy);

        Transaction transaction = service.createTransaction();

        assertNotNull(transaction.getId());
        assertEquals("IN_PROGRESS", transaction.getStatus().name());
    }

    @Test
    void testMoveToPayment_shouldUpdateStateToAwaitingPayment() {
        Transaction trx = new Transaction();
        when(repository.findById(trx.getId())).thenReturn(trx);
        when(repository.save(any(Transaction.class))).thenReturn(trx);

        service.moveToPayment(trx.getId());

        assertEquals("AWAITING_PAYMENT", trx.getStatus().name());
    }
}
