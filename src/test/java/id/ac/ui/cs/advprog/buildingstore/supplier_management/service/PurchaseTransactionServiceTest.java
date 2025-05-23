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

    @Test
    void getTransactionsBySupplier_shouldReturnOnlyMatchingTransactions() {
        Supplier supplier1 = Supplier.builder().id(1L).name("PT Semen").category(SupplierCategory.SEMEN).build();
        Supplier supplier2 = Supplier.builder().id(2L).name("PT Kayu").category(SupplierCategory.KAYU).build();

        PurchaseTransaction tx1 = PurchaseTransaction.builder()
                .supplier(supplier1)
                .productName("Semen A")
                .quantity(50)
                .totalPrice(new BigDecimal("1000000"))
                .date(LocalDateTime.now())
                .build();

        PurchaseTransaction tx2 = PurchaseTransaction.builder()
                .supplier(supplier2)
                .productName("Kayu Jati")
                .quantity(30)
                .totalPrice(new BigDecimal("800000"))
                .date(LocalDateTime.now())
                .build();

        when(transactionRepo.findAll()).thenReturn(List.of(tx1, tx2));

        var result = service.getTransactionsBySupplier(supplier1);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(tx1);
    }

}
