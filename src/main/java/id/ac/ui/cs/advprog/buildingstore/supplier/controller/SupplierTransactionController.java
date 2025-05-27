package id.ac.ui.cs.advprog.buildingstore.supplier.controller;

import id.ac.ui.cs.advprog.buildingstore.supplier.dto.PurchaseTransactionDTO;
import id.ac.ui.cs.advprog.buildingstore.supplier.model.Supplier;
import id.ac.ui.cs.advprog.buildingstore.supplier.service.PurchaseTransactionService;
import id.ac.ui.cs.advprog.buildingstore.supplier.service.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Controller
@RequestMapping("/supplier")
@RequiredArgsConstructor
public class SupplierTransactionController {

    private final PurchaseTransactionService transactionService;
    private final SupplierService supplierService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/transactions")
    public CompletableFuture<String> showSupplierTransactions(@PathVariable("id") Long supplierId, Model model) {
        Supplier supplier = supplierService.findById(supplierId);
        model.addAttribute("supplier", supplier);
        log.info("Fetching async transactions for supplier id={}", supplierId);

        return transactionService.getTransactionsBySupplierAsync(supplier)
                .thenApply(transactions -> {
                    model.addAttribute("transactions", transactions);
                    log.info("Retrieved {} transactions for supplier id={}", transactions.size(), supplierId);
                    return "supplier/supplier_transactions";
                })
                .exceptionally(ex -> {
                    Throwable cause = ex.getCause();
                    if (cause instanceof InterruptedException) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Thread interrupted while fetching transactions", cause);
                    }
                    throw new RuntimeException("Failed to fetch transactions asynchronously", cause);
                });
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/transactions/add")
    public String showAddTransactionForm(@PathVariable("id") Long supplierId, Model model) {
        Supplier supplier = supplierService.findById(supplierId);
        model.addAttribute("supplier", supplier);
        model.addAttribute("transactionDTO", new PurchaseTransactionDTO());
        log.info("Opening add transaction form for supplier id={}", supplierId);
        return "supplier/add_transaction";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/transactions/add")
    public String addTransaction(@PathVariable("id") Long supplierId,
                                 @ModelAttribute PurchaseTransactionDTO dto) {
        Supplier supplier = supplierService.findById(supplierId);
        dto.setSupplier(supplier);
        transactionService.addTransaction(dto);
        log.info("Added transaction for supplier id={} | product={} | qty={}",
                supplierId, dto.getProductName(), dto.getQuantity());
        return "redirect:/supplier/" + supplierId + "/transactions";
    }
}
