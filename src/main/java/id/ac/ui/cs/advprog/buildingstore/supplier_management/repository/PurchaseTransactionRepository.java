package id.ac.ui.cs.advprog.buildingstore.supplier_management.repository;

import id.ac.ui.cs.advprog.buildingstore.supplier_management.model.PurchaseTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import id.ac.ui.cs.advprog.buildingstore.supplier_management.model.Supplier;
import java.util.List;

public interface PurchaseTransactionRepository extends JpaRepository<PurchaseTransaction, Long> {
    List<PurchaseTransaction> findBySupplier(Supplier supplier);
}