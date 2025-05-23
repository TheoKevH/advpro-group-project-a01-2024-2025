package id.ac.ui.cs.advprog.buildingstore.supplier_management.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PurchaseTransactionTest {

    @Test
    void canCreateTransaction() {
        Supplier supplier = Supplier.builder()
                .id(1L)
                .name("PT Kayu Jaya")
                .category(SupplierCategory.KAYU)
                .build();

        PurchaseTransaction tx = PurchaseTransaction.builder()
                .supplier(supplier)
                .productName("Kayu Jati")
                .quantity(100)
                .totalPrice(new BigDecimal("7500000"))
                .date(LocalDateTime.now())
                .build();

        assertEquals("Kayu Jati", tx.getProductName());
        assertEquals(100, tx.getQuantity());
        assertEquals(new BigDecimal("7500000"), tx.getTotalPrice());
        assertEquals(supplier, tx.getSupplier());
        assertNotNull(tx.getDate());
    }
}
