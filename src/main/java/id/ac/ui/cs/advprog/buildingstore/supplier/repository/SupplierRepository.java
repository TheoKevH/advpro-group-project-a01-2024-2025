package id.ac.ui.cs.advprog.buildingstore.supplier.repository;

import id.ac.ui.cs.advprog.buildingstore.supplier.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}
