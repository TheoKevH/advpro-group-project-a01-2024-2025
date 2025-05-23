package id.ac.ui.cs.advprog.buildingstore.supplier_management.repository;

import id.ac.ui.cs.advprog.buildingstore.supplier_management.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PurchaseTransactionRepositoryTest {

    @Autowired
    private PurchaseTransactionRepository transactionRepository;

    @Test
    void canSaveAndFindTransaction() {
        Supplier supplier = Supplier.builder()
                .name("PT Baja Besi")
                .category(SupplierCategory.BESI_BAJA)
                .build();

        PurchaseTransaction tx = PurchaseTransaction.builder()
                .supplier(supplier)
                .productName("Besi Hollow 4x4")
                .quantity(200)
                .totalPrice(new BigDecimal("3000000"))
                .date(LocalDateTime.now())
                .build();

        PurchaseTransaction saved = transactionRepository.save(tx);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getSupplier().getName()).isEqualTo("PT Baja Besi");
        assertThat(saved.getProductName()).isEqualTo("Besi Hollow 4x4");
    }
}
