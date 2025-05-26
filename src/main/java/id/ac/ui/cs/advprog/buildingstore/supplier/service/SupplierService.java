package id.ac.ui.cs.advprog.buildingstore.supplier.service;

import id.ac.ui.cs.advprog.buildingstore.supplier.dto.SupplierDTO;
import id.ac.ui.cs.advprog.buildingstore.supplier.model.Supplier;
import java.util.List;


public interface SupplierService {
    void addSupplier(SupplierDTO dto);
    void editSupplier(Long id, SupplierDTO dto);
    void deleteSupplier(Long id);
    List<Supplier> getAllSuppliers();
    Supplier findById(Long id);
}
