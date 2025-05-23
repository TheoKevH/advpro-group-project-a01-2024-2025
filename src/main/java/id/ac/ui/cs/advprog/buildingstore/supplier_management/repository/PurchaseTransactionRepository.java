package id.ac.ui.cs.advprog.buildingstore.supplier_management.repository;

import id.ac.ui.cs.advprog.buildingstore.supplier_management.model.PurchaseTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseTransactionRepository extends JpaRepository<PurchaseTransaction, Long> {
}