package id.ac.ui.cs.advprog.buildingstore.transaksi.service;

import id.ac.ui.cs.advprog.buildingstore.transaksi.enums.TransactionStatus;
import id.ac.ui.cs.advprog.buildingstore.transaksi.model.Transaction;
import id.ac.ui.cs.advprog.buildingstore.transaksi.model.TransactionItem;
import id.ac.ui.cs.advprog.buildingstore.transaksi.repository.InMemoryTransactionRepository;
import id.ac.ui.cs.advprog.buildingstore.transaksi.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.UUID;

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
    void testMoveToPayment_shouldUpdateStateToAwaitingPayment() {
        Transaction trx = Transaction.builder()
                .customerId("cust-1")
                .items(List.of(new TransactionItem("prod-1", 1)))
                .build();

        when(repository.findById(trx.getTransactionId())).thenReturn(trx);
        when(repository.save(any(Transaction.class))).thenReturn(trx);

        service.moveToPayment(trx.getTransactionId());

        assertEquals("AWAITING_PAYMENT", trx.getStatus().name());
    }

    @Test
    void testCreateTransaction_withItems_shouldStoreCorrectly() {
        List<TransactionItem> items = List.of(
                new TransactionItem("prod-12", 2),
                new TransactionItem("prod-34", 1)
        );
        String customerId = "cust-789";

        Transaction trx = Transaction.builder()
                .customerId(customerId)
                .items(items)
                .build();

        when(repository.save(any(Transaction.class))).thenReturn(trx);

        Transaction result = service.createTransaction(customerId, items);

        assertNotNull(result.getTransactionId());
        assertEquals(customerId, result.getCustomerId());
        assertEquals(TransactionStatus.IN_PROGRESS, result.getStatus());
        assertEquals(2, result.getItems().size());
        assertEquals("prod-12", result.getItems().get(0).getProductId());
        assertEquals(2, result.getItems().get(0).getQuantity());
    }

    @Test
    void testMarkAsPaid_directlyFromInProgress_shouldThrowException() {
        Transaction trx = Transaction.builder()
                .customerId("cust-123")
                .items(List.of(new TransactionItem("prod-1", 1)))
                .build();

        when(repository.findById(trx.getTransactionId())).thenReturn(trx);

        assertThrows(IllegalStateException.class, () -> {
            service.markAsPaid(trx.getTransactionId());
        });
    }

    @Test
    void testCancelAfterCompleted_shouldThrowException() {
        Transaction trx = Transaction.builder()
                .customerId("cust-222")
                .items(List.of(new TransactionItem("prod-9", 1)))
                .build();

        trx.moveToPayment();
        trx.markAsPaid();

        when(repository.findById(trx.getTransactionId())).thenReturn(trx);

        assertThrows(IllegalStateException.class, () -> {
            service.cancelTransaction(trx.getTransactionId());
        });
    }

    @Test
    void testUpdateTransaction_shouldReplaceItems() {
        String id = UUID.randomUUID().toString();
        Transaction trx = Transaction.builder()
                .transactionId(id)
                .customerId("cust-1")
                .items(List.of(new TransactionItem("prod-1", 2)))
                .build();

        List<TransactionItem> updatedItems = List.of(
                new TransactionItem("prod-1", 5),
                new TransactionItem("prod-2", 3)
        );

        when(repository.findById(id)).thenReturn(trx);
        when(repository.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));

        Transaction result = service.updateTransaction(id, updatedItems);

        assertEquals(2, result.getItems().size());
        assertEquals("prod-2", result.getItems().get(1).getProductId());
        assertEquals(3, result.getItems().get(1).getQuantity());
    }

    @Test
    void testGetTransactionsByCustomer_shouldReturnOnlyMatchingTransactions() {
        Transaction trx1 = Transaction.builder()
                .transactionId("trx-1")
                .customerId("cust-123")
                .build();

        Transaction trx2 = Transaction.builder()
                .transactionId("trx-2")
                .customerId("cust-999")
                .build();

        when(repository.findAll()).thenReturn(List.of(trx1, trx2));

        List<Transaction> result = service.getTransactionsByCustomer("cust-123");

        assertEquals(1, result.size());
        assertEquals("trx-1", result.get(0).getTransactionId());
    }

    @Test
    void testCreateTransaction_shouldStoreCreatedByUser() {
        String username = "kasir01";
        Authentication auth = new UsernamePasswordAuthenticationToken(username, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        User user = new User();
        user.setId(1L);
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(repository.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));

        Transaction result = service.createTransaction("cust-123", List.of(new TransactionItem("prod-1", 1)));

        assertNotNull(result.getCreatedBy());
        assertEquals("kasir01", result.getCreatedBy().getUsername());
    }

    @Test
    void testGetTransactionsByUser_shouldReturnOnlyTheirOwn() {
        User user = new User();
        user.setId(1L);
        user.setUsername("kasir01");

        Transaction t1 = Transaction.builder().transactionId("trx-1").createdBy(user).build();
        Transaction t2 = Transaction.builder().transactionId("trx-2").createdBy(new User()).build();

        when(repository.findAll()).thenReturn(List.of(t1, t2));

        List<Transaction> result = service.getTransactionsByUser(user);

        assertEquals(1, result.size());
        assertEquals("trx-1", result.get(0).getTransactionId());
    }





}

