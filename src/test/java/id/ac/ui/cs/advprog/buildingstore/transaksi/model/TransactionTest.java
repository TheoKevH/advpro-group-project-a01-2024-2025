package id.ac.ui.cs.advprog.buildingstore.transaksi.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransactionTest {

    private Transaction transaction;

    @BeforeEach
    void setUp() {
        transaction = new Transaction();
    }

    @Test
    void testTransactionInitialStatus(){
        assertEquals(TransactionStatus.IN_PROGRESS, transaction.getState().getStatus());
    }

    @Test
    void testTransactionMoveToPaymentStatus() {
        transaction.moveToPayment();
        assertEquals(TransactionStatus.AWAITING_PAYMENT, transaction.getState().getStatus());
    }

    @Test
    void testTransactionCancelStatus() {
        transaction.cancel();
        assertEquals(TransactionStatus.CANCELLED, transaction.getState().getStatus());
    }
}
