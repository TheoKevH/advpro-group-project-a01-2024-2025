package id.ac.ui.cs.advprog.buildingstore.supplier.service;

import id.ac.ui.cs.advprog.buildingstore.supplier.dto.SupplierDTO;
import id.ac.ui.cs.advprog.buildingstore.supplier.factory.SupplierFactory;
import id.ac.ui.cs.advprog.buildingstore.supplier.model.Supplier;
import id.ac.ui.cs.advprog.buildingstore.supplier.repository.SupplierRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Slf4j
@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository repo;

    public SupplierServiceImpl(SupplierRepository repo) {
        this.repo = repo;
    }

    @Override
    public void addSupplier(SupplierDTO dto) {
        validateDTO(dto);
        Supplier supplier = SupplierFactory.fromDTO(dto);
        log.info("Saving new supplier: {}", supplier.getName());
        repo.save(supplier);
    }

    @Override
    public void editSupplier(Long id, SupplierDTO dto) {
        validateId(id);
        validateDTO(dto);

        Supplier supplier = getSupplierOrThrow(id);
        updateSupplierFromDTO(supplier, dto);

        log.info("Updating supplier with id {}: {}", id, dto.getName());
        repo.save(supplier);
    }

    @Override
    public void deleteSupplier(Long id) {
        validateId(id);
        checkSupplierExists(id);

        log.info("Deleting supplier with id {}", id);
        repo.deleteById(id);
    }

    @Override
    public Page<Supplier> getAllSuppliers(Pageable pageable) {
        Page<Supplier> suppliers = repo.findAll(pageable);
        log.info("Retrieved {} suppliers from database (page: {})", suppliers.getNumberOfElements(), pageable.getPageNumber());
        return suppliers;
    }

    @Override
    public Supplier findById(Long id) {
        validateId(id);
        return getSupplierOrThrow(id);
    }

    private void validateDTO(SupplierDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("SupplierDTO cannot be null");
        }
    }

    private void validateId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Supplier ID cannot be null");
        }
    }

    private Supplier getSupplierOrThrow(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Supplier with id " + id + " not found"));
    }

    private void checkSupplierExists(Long id) {
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("Supplier with id " + id + " not found");
        }
    }

    private void updateSupplierFromDTO(Supplier supplier, SupplierDTO dto) {
        supplier.setName(dto.getName());
        supplier.setAddress(dto.getAddress());
        supplier.setContact(dto.getContact());
        supplier.setCategory(dto.getCategory());
    }
}
