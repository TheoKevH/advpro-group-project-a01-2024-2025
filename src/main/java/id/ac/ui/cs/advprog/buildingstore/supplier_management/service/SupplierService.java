package id.ac.ui.cs.advprog.buildingstore.supplier_management.service;

import id.ac.ui.cs.advprog.buildingstore.supplier_management.dto.SupplierDTO;
import id.ac.ui.cs.advprog.buildingstore.supplier_management.factory.SupplierFactory;
import id.ac.ui.cs.advprog.buildingstore.supplier_management.model.Supplier;
import id.ac.ui.cs.advprog.buildingstore.supplier_management.repository.SupplierRepository;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public interface SupplierService {
    void addSupplier(SupplierDTO dto);
    void editSupplier(Long id, SupplierDTO dto);
    void deleteSupplier(Long id);
    List<Supplier> getAllSuppliers();
}
