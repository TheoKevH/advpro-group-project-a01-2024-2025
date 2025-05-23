package id.ac.ui.cs.advprog.buildingstore.supplier_management.controller;

import id.ac.ui.cs.advprog.buildingstore.supplier_management.dto.PurchaseTransactionDTO;
import id.ac.ui.cs.advprog.buildingstore.supplier_management.model.Supplier;
import id.ac.ui.cs.advprog.buildingstore.supplier_management.service.PurchaseTransactionService;
import id.ac.ui.cs.advprog.buildingstore.supplier_management.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/supplier")
@RequiredArgsConstructor
public class TransactionController {

    private final PurchaseTransactionService transactionService;
    private final SupplierService supplierService;

    @GetMapping("/{id}/transactions")
    public String showSupplierTransactions(@PathVariable Long id, Model model) {
        Supplier supplier = supplierService.findById(id);
        var transactions = transactionService.getTransactionsBySupplier(supplier);
        model.addAttribute("supplier", supplier);
        model.addAttribute("transactions", transactions);
        return "admin/supplier_transactions";
    }
}
