package id.ac.ui.cs.advprog.buildingstore.supplier.service;

import id.ac.ui.cs.advprog.buildingstore.supplier.dto.SupplierDTO;
import id.ac.ui.cs.advprog.buildingstore.supplier.model.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface SupplierService {
    void addSupplier(SupplierDTO dto);
    void editSupplier(Long id, SupplierDTO dto);
    void deleteSupplier(Long id);
    Page<Supplier> getAllSuppliers(Pageable pageable);
    Supplier findById(Long id);
}
