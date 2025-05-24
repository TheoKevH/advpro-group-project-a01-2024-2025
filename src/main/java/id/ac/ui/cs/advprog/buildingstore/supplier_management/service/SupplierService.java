package id.ac.ui.cs.advprog.buildingstore.supplier_management.service;

import id.ac.ui.cs.advprog.buildingstore.supplier_management.dto.SupplierDTO;
import id.ac.ui.cs.advprog.buildingstore.supplier_management.model.Supplier;
import java.util.List;


public interface SupplierService {
    void addSupplier(SupplierDTO dto);
    void editSupplier(Long id, SupplierDTO dto);
    void deleteSupplier(Long id);
    List<Supplier> getAllSuppliers();
    Supplier findById(Long id);
}
