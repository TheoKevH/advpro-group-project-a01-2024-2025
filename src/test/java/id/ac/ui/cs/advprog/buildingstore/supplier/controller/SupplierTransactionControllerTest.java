package id.ac.ui.cs.advprog.buildingstore.supplier.controller;

import id.ac.ui.cs.advprog.buildingstore.supplier.dto.PurchaseTransactionDTO;
import id.ac.ui.cs.advprog.buildingstore.supplier.model.*;
import id.ac.ui.cs.advprog.buildingstore.supplier.service.PurchaseTransactionService;
import id.ac.ui.cs.advprog.buildingstore.supplier.service.SupplierService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

class SupplierTransactionControllerTest {

    @InjectMocks
    private SupplierTransactionController controller;

    @Mock
    private PurchaseTransactionService transactionService;

    @Mock
    private SupplierService supplierService;

    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void showSupplierTransactions_shouldAddAttributesAndReturnView() throws Exception {
        Supplier supplier = Supplier.builder()
                .id(1L)
                .name("PT Baja Utama")
                .category(SupplierCategory.BESI_BAJA)
                .build();

        List<PurchaseTransaction> transactions = List.of(
                PurchaseTransaction.builder()
                        .supplier(supplier)
                        .productName("Besi Beton")
                        .quantity(20)
                        .totalPrice(new BigDecimal("2000000"))
                        .date(LocalDateTime.now())
                        .build()
        );

        when(supplierService.findById(1L)).thenReturn(supplier);
        when(transactionService.getTransactionsBySupplierAsync(supplier))
                .thenReturn(CompletableFuture.completedFuture(transactions));

        String result = controller.showSupplierTransactions(1L, model).get();

        verify(model).addAttribute("supplier", supplier);
        verify(model).addAttribute("transactions", transactions);
        assertThat(result).isEqualTo("supplier/supplier_transactions");
    }

    @Test
    void showSupplierTransactions_shouldThrowRuntimeException_whenAsyncFails() {
        Long supplierId = 1L;
        Supplier supplier = Supplier.builder()
                .id(supplierId)
                .name("PT Error")
                .build();

        when(supplierService.findById(supplierId)).thenReturn(supplier);
        CompletableFuture<List<PurchaseTransaction>> failingFuture = new CompletableFuture<>();
        failingFuture.completeExceptionally(new RuntimeException("Async failed"));

        when(transactionService.getTransactionsBySupplierAsync(supplier)).thenReturn(failingFuture);

        Throwable thrown = catchThrowable(() ->
                controller.showSupplierTransactions(supplierId, model).join()
        );

        assertThat(thrown)
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to fetch transactions asynchronously")
                .hasCauseInstanceOf(RuntimeException.class)
                .hasRootCauseMessage("Async failed");
    }



    @Test
    void showAddTransactionForm_shouldAddAttributesAndReturnView() {
        Long supplierId = 1L;
        Supplier supplier = Supplier.builder()
                .id(supplierId)
                .name("PT Tambah")
                .category(SupplierCategory.PLUMBING)
                .build();

        when(supplierService.findById(supplierId)).thenReturn(supplier);

        String result = controller.showAddTransactionForm(supplierId, model);

        verify(model).addAttribute(eq("supplier"), eq(supplier));
        verify(model).addAttribute(eq("transactionDTO"), any(PurchaseTransactionDTO.class));
        assertThat(result).isEqualTo("supplier/add_transaction");
    }

    @Test
    void addTransaction_shouldCallServiceAndRedirectToTransactionList() {
        Long supplierId = 1L;

        Supplier supplier = Supplier.builder()
                .id(supplierId)
                .name("PT Semen Maju")
                .category(SupplierCategory.SEMEN)
                .build();

        PurchaseTransactionDTO dto = new PurchaseTransactionDTO();
        dto.setProductName("Semen Gresik");
        dto.setQuantity(100);
        dto.setTotalPrice(new BigDecimal("1000000"));
        dto.setDate(LocalDateTime.now());

        when(supplierService.findById(supplierId)).thenReturn(supplier);

        String result = controller.addTransaction(supplierId, dto);

        verify(transactionService).addTransaction(dto);
        assertThat(result).isEqualTo("redirect:/supplier/" + supplierId + "/transactions");
    }
}
