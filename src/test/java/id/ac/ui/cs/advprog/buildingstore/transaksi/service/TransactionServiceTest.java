package id.ac.ui.cs.advprog.buildingstore.transaksi.service;

import id.ac.ui.cs.advprog.buildingstore.transaksi.model.Transaction;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TransactionServiceTest {

    @Test
    void testCreateTransaction_shouldInitializeWithInProgressState() {
        TransactionService service = new TransactionServiceImpl();
        Transaction transaction = service.createTransaction();

        assertNotNull(transaction.getId());
        assertEquals("IN_PROGRESS", transaction.getStatus().name());
    }

    @Test
    void testMoveToPayment_shouldUpdateStateToAwaitingPayment() {
        TransactionService service = new TransactionServiceImpl();
        Transaction trx = service.createTransaction();

        service.moveToPayment(trx.getId());
        assertEquals("AWAITING_PAYMENT", trx.getStatus().name());
    }

}
