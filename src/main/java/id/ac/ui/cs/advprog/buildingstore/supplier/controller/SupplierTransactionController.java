package id.ac.ui.cs.advprog.buildingstore.supplier.controller;

import id.ac.ui.cs.advprog.buildingstore.supplier.dto.PurchaseTransactionDTO;
import id.ac.ui.cs.advprog.buildingstore.supplier.model.Supplier;
import id.ac.ui.cs.advprog.buildingstore.supplier.model.PurchaseTransaction;
import id.ac.ui.cs.advprog.buildingstore.supplier.service.PurchaseTransactionService;
import id.ac.ui.cs.advprog.buildingstore.supplier.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("/supplier")
@RequiredArgsConstructor
public class SupplierTransactionController {

    private final PurchaseTransactionService transactionService;
    private final SupplierService supplierService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/transactions")
    public String showSupplierTransactions(@PathVariable("id") Long supplierId, Model model) {
        try {
            Supplier supplier = supplierService.findById(supplierId);
            List<PurchaseTransaction> transactions = transactionService.getTransactionsBySupplierAsync(supplier).get();

            model.addAttribute("supplier", supplier);
            model.addAttribute("transactions", transactions);
            return "supplier/supplier_transactions";
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Gagal mengambil data transaksi supplier", e);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/transactions/add")
    public String showAddTransactionForm(@PathVariable("id") Long supplierId, Model model) {
        Supplier supplier = supplierService.findById(supplierId);
        model.addAttribute("supplier", supplier);
        model.addAttribute("transactionDTO", new PurchaseTransactionDTO());
        return "supplier/add_transaction";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/transactions/add")
    public String addTransaction(@PathVariable("id") Long supplierId,
                                 @ModelAttribute PurchaseTransactionDTO dto) {
        Supplier supplier = supplierService.findById(supplierId);
        dto.setSupplier(supplier);
        transactionService.addTransaction(dto);
        return "redirect:/supplier/" + supplierId + "/transactions";
    }

}
