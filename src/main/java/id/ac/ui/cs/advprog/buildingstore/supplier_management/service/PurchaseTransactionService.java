package id.ac.ui.cs.advprog.buildingstore.supplier_management.service;

import id.ac.ui.cs.advprog.buildingstore.supplier_management.dto.PurchaseTransactionDTO;
import id.ac.ui.cs.advprog.buildingstore.supplier_management.model.PurchaseTransaction;
import id.ac.ui.cs.advprog.buildingstore.supplier_management.model.Supplier;
import java.util.List;

public interface PurchaseTransactionService {
    void addTransaction(PurchaseTransactionDTO dto);
    List<PurchaseTransaction> getTransactionsBySupplier(Supplier supplier);
}