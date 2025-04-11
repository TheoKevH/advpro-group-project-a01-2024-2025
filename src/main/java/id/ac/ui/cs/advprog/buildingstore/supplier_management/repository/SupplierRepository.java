package id.ac.ui.cs.advprog.buildingstore.supplier_management.repository;

import id.ac.ui.cs.advprog.buildingstore.supplier_management.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}
