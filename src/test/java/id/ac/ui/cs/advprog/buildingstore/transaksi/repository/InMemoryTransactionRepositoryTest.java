package id.ac.ui.cs.advprog.buildingstore.transaksi.repository;

import id.ac.ui.cs.advprog.buildingstore.transaksi.model.Transaction;
import id.ac.ui.cs.advprog.buildingstore.transaksi.model.TransactionItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTransactionRepositoryTest {

    private InMemoryTransactionRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryTransactionRepository();
    }

    @Test
    void testSave_shouldStoreTransaction() {
        Transaction trx = Transaction.builder()
                .customerId("cust-1")
                .items(List.of(new TransactionItem("prod-1", 2)))
                .build();

        Transaction saved = repository.save(trx);
        assertEquals(trx.getTransactionId(), saved.getTransactionId());
        assertEquals(trx, repository.findById(trx.getTransactionId()));
    }

    @Test
    void testFindById_shouldReturnCorrectTransaction() {
        Transaction trx = Transaction.builder().customerId("cust-x").build();
        repository.save(trx);

        Transaction found = repository.findById(trx.getTransactionId());
        assertNotNull(found);
        assertEquals("cust-x", found.getCustomerId());
    }

    @Test
    void testFindAll_shouldReturnAllTransactions() {
        Transaction t1 = Transaction.builder().customerId("c1").build();
        Transaction t2 = Transaction.builder().customerId("c2").build();
        repository.save(t1);
        repository.save(t2);

        List<Transaction> all = repository.findAll();
        assertEquals(2, all.size());
        assertTrue(all.contains(t1));
        assertTrue(all.contains(t2));
    }

    @Test
    void testDelete_shouldRemoveTransaction() {
        Transaction trx = Transaction.builder().customerId("to-delete").build();
        repository.save(trx);

        assertNotNull(repository.findById(trx.getTransactionId()));
        repository.delete(trx.getTransactionId());
        assertNull(repository.findById(trx.getTransactionId()));
    }

    @Test
    void testDelete_unknownId_shouldNotThrow() {
        assertDoesNotThrow(() -> repository.delete("non-existent-id"));
    }

    @Test
    void testFindById_unknownId_shouldReturnNull() {
        assertNull(repository.findById("unknown-id"));
    }

    @Test
    void testFindAll_whenEmpty_shouldReturnEmptyList() {
        List<Transaction> all = repository.findAll();
        assertNotNull(all);
        assertTrue(all.isEmpty());
    }
}
