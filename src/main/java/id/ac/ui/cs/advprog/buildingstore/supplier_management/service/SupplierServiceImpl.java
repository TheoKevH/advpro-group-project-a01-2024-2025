package id.ac.ui.cs.advprog.buildingstore.supplier_management.service;

import id.ac.ui.cs.advprog.buildingstore.supplier_management.dto.SupplierDTO;
import id.ac.ui.cs.advprog.buildingstore.supplier_management.factory.SupplierFactory;
import id.ac.ui.cs.advprog.buildingstore.supplier_management.model.Supplier;
import id.ac.ui.cs.advprog.buildingstore.supplier_management.repository.SupplierRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository repo;

    public SupplierServiceImpl(SupplierRepository repo) {
        this.repo = repo;
    }

    @Override
    public void addSupplier(SupplierDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("SupplierDTO cannot be null");
        }

        Supplier supplier = SupplierFactory.fromDTO(dto);
        log.info("Saving supplier: {}", supplier.getName());
        repo.save(supplier);
    }

    @Override
    public void editSupplier(Long id, SupplierDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("SupplierDTO cannot be null");
        }

        Supplier existingSupplier = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found"));

        existingSupplier.setName(dto.getName());
        existingSupplier.setAddress(dto.getAddress());
        existingSupplier.setContact(dto.getContact());
        existingSupplier.setCategory(dto.getCategory());

        log.info("Updating supplier with id {}: {}", id, dto.getName());
        repo.save(existingSupplier);
    }

}
