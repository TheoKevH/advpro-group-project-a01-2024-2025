package id.ac.ui.cs.advprog.buildingstore.supplier.service;

import id.ac.ui.cs.advprog.buildingstore.supplier.dto.PurchaseTransactionDTO;
import id.ac.ui.cs.advprog.buildingstore.supplier.model.*;
import id.ac.ui.cs.advprog.buildingstore.supplier.repository.PurchaseTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class PurchaseTransactionServiceTest {

    @Mock
    private PurchaseTransactionRepository transactionRepo;

    @Mock
    private RestTemplate restTemplate;

    private PurchaseTransactionServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new PurchaseTransactionServiceImpl(transactionRepo, restTemplate);
    }

    @Test
    void canAddTransaction() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        Supplier supplier = Supplier.builder()
                .id(1L)
                .name("PT Beton Jaya")
                .category(SupplierCategory.SEMEN)
                .build();

        PurchaseTransactionDTO dto = new PurchaseTransactionDTO(
                "Semen Gresik 40kg",
                120,
                new BigDecimal("1800000"),
                LocalDateTime.now(),
                supplier
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

        PurchaseTransaction tx1 = PurchaseTransaction.builder()
                .supplier(supplier1)
                .productName("Semen A")
                .quantity(50)
                .totalPrice(new BigDecimal("1000000"))
                .date(LocalDateTime.now())
                .build();

        when(transactionRepo.findBySupplier(supplier1)).thenReturn(List.of(tx1));

        var result = service.getTransactionsBySupplier(supplier1);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(tx1);
    }

    @Test
    void getTransactionsBySupplierAsync_shouldReturnFutureWithResult() throws Exception {
        Supplier supplier = Supplier.builder().id(1L).name("PT Semen").build();
        PurchaseTransaction tx = PurchaseTransaction.builder()
                .supplier(supplier)
                .productName("Semen A")
                .quantity(10)
                .totalPrice(new BigDecimal("1000000"))
                .date(LocalDateTime.now())
                .build();

        when(transactionRepo.findBySupplier(supplier)).thenReturn(List.of(tx));

        var future = service.getTransactionsBySupplierAsync(supplier);

        assertThat(future).isNotNull();
        assertThat(future.get()).containsExactly(tx);
    }

}
