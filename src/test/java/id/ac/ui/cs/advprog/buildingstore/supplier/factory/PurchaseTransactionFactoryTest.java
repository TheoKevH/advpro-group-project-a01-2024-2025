package id.ac.ui.cs.advprog.buildingstore.supplier.factory;

import id.ac.ui.cs.advprog.buildingstore.supplier.dto.PurchaseTransactionDTO;
import id.ac.ui.cs.advprog.buildingstore.supplier.model.PurchaseTransaction;
import id.ac.ui.cs.advprog.buildingstore.supplier.model.Supplier;
import id.ac.ui.cs.advprog.buildingstore.supplier.model.SupplierCategory;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

public class PurchaseTransactionFactoryTest {

    @Test
    void createTransaction_shouldMapFieldsCorrectly() {
        Supplier supplier = Supplier.builder()
                .id(1L)
                .name("PT Test")
                .category(SupplierCategory.KAYU)
                .build();

        PurchaseTransactionDTO dto = new PurchaseTransactionDTO(
                "Kayu Jati", 10, new BigDecimal("1000000"), LocalDateTime.now(), supplier
        );

        PurchaseTransaction tx = PurchaseTransactionFactory.fromDTO(dto);

        assertThat(tx.getProductName()).isEqualTo("Kayu Jati");
        assertThat(tx.getQuantity()).isEqualTo(10);
        assertThat(tx.getTotalPrice()).isEqualTo("1000000");
        assertThat(tx.getSupplier()).isEqualTo(supplier);
    }

    @Test
    void createTransaction_shouldThrowIfSupplierIsNull() {
        PurchaseTransactionDTO dto = new PurchaseTransactionDTO(
                "Produk A", 5, new BigDecimal("50000"), LocalDateTime.now(), null
        );

        assertThatThrownBy(() -> PurchaseTransactionFactory.fromDTO(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Supplier must not be null");
    }

    @Test
    void createTransaction_shouldThrowIfDtoIsNull() {
        assertThatThrownBy(() -> PurchaseTransactionFactory.fromDTO(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
