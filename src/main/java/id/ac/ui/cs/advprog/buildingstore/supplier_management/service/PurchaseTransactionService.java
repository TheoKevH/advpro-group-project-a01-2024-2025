package id.ac.ui.cs.advprog.buildingstore.supplier_management.service;

import id.ac.ui.cs.advprog.buildingstore.supplier_management.dto.PurchaseTransactionDTO;
import id.ac.ui.cs.advprog.buildingstore.supplier_management.model.PurchaseTransaction;
import id.ac.ui.cs.advprog.buildingstore.supplier_management.model.Supplier;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface PurchaseTransactionService {
    void addTransaction(PurchaseTransactionDTO dto);
    List<PurchaseTransaction> getTransactionsBySupplier(Supplier supplier);
    @Async
    CompletableFuture<List<PurchaseTransaction>> getTransactionsBySupplierAsync(Supplier supplier);
}