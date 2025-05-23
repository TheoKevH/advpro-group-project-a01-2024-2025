package id.ac.ui.cs.advprog.buildingstore.supplier_management.controller;

import id.ac.ui.cs.advprog.buildingstore.supplier_management.dto.PurchaseTransactionDTO;
import id.ac.ui.cs.advprog.buildingstore.supplier_management.model.Supplier;
import id.ac.ui.cs.advprog.buildingstore.supplier_management.model.PurchaseTransaction;
import id.ac.ui.cs.advprog.buildingstore.supplier_management.service.PurchaseTransactionService;
import id.ac.ui.cs.advprog.buildingstore.supplier_management.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Controller
@RequestMapping("/supplier")
@RequiredArgsConstructor
public class SupplierTransactionController {

    private final PurchaseTransactionService transactionService;
    private final SupplierService supplierService;

    @GetMapping("/{id}/transactions")
    public String showSupplierTransactions(@PathVariable("id") Long supplierId, Model model) throws Exception {
        Supplier supplier = supplierService.findById(supplierId);
        CompletableFuture<List<PurchaseTransaction>> future = transactionService.getTransactionsBySupplierAsync(supplier);
        List<PurchaseTransaction> transactions = future.get();

        model.addAttribute("supplier", supplier);
        model.addAttribute("transactions", transactions);
        return "admin/supplier_transactions";
    }

    @GetMapping("/{id}/transactions/add")
    public String showAddTransactionForm(@PathVariable("id") Long supplierId, Model model) {
        Supplier supplier = supplierService.findById(supplierId);
        model.addAttribute("supplier", supplier);
        model.addAttribute("transactionDTO", new PurchaseTransactionDTO());
        return "admin/add_transaction";
    }

    @PostMapping("/{id}/transactions/add")
    public String addTransaction(@PathVariable("id") Long supplierId,
                                 @ModelAttribute PurchaseTransactionDTO dto) {
        Supplier supplier = supplierService.findById(supplierId);
        dto.setSupplier(supplier);
        transactionService.addTransaction(dto);
        return "redirect:/supplier/" + supplierId + "/transactions";
    }

}
