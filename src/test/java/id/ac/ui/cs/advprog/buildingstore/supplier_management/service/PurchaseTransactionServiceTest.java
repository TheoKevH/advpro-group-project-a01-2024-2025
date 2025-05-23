package id.ac.ui.cs.advprog.buildingstore.supplier_management.service;

import id.ac.ui.cs.advprog.buildingstore.supplier_management.dto.PurchaseTransactionDTO;
import id.ac.ui.cs.advprog.buildingstore.supplier_management.model.*;
import id.ac.ui.cs.advprog.buildingstore.supplier_management.repository.PurchaseTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

class PurchaseTransactionServiceTest {

    @Mock
    private PurchaseTransactionRepository transactionRepo;

    @InjectMocks
    private PurchaseTransactionServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void canAddTransaction() {
        Supplier supplier = Supplier.builder()
                .id(1L)
                .name("PT Beton Jaya")
                .category(SupplierCategory.SEMEN)
                .build();

        PurchaseTransactionDTO dto = new PurchaseTransactionDTO(
                supplier,
                "Semen Gresik 40kg",
                120,
                new BigDecimal("1800000"),
                LocalDateTime.now()
        );

        service.addTransaction(dto);

        ArgumentCaptor<PurchaseTransaction> captor = ArgumentCaptor.forClass(PurchaseTransaction.class);
        verify(transactionRepo, times(1)).save(captor.capture());

        PurchaseTransaction saved = captor.getValue();
        assertThat(saved.getProductName()).isEqualTo("Semen Gresik 40kg");
        assertThat(saved.getQuantity()).isEqualTo(120);
        assertThat(saved.getSupplier()).isEqualTo(supplier);
    }
}
