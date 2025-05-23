package id.ac.ui.cs.advprog.buildingstore.supplier_management.controller;

import id.ac.ui.cs.advprog.buildingstore.supplier_management.dto.PurchaseTransactionDTO;
import id.ac.ui.cs.advprog.buildingstore.supplier_management.model.*;
import id.ac.ui.cs.advprog.buildingstore.supplier_management.service.PurchaseTransactionService;
import id.ac.ui.cs.advprog.buildingstore.supplier_management.service.SupplierService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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
    void showSupplierTransactions_shouldAddAttributesAndReturnView() {
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
        when(transactionService.getTransactionsBySupplier(supplier)).thenReturn(transactions);

        String result = controller.showSupplierTransactions(1L, model);

        verify(model).addAttribute("supplier", supplier);
        verify(model).addAttribute("transactions", transactions);
        assertThat(result).isEqualTo("admin/supplier_transactions");
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
