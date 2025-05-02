package id.ac.ui.cs.advprog.buildingstore.supplier_management.service;

import id.ac.ui.cs.advprog.buildingstore.supplier_management.dto.SupplierDTO;
import id.ac.ui.cs.advprog.buildingstore.supplier_management.model.Supplier;
import id.ac.ui.cs.advprog.buildingstore.supplier_management.repository.SupplierRepository;
import id.ac.ui.cs.advprog.buildingstore.supplier_management.service.SupplierService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SupplierServiceTest {

    /*@Mock
    private SupplierRepository supplierRepository;

    @InjectMocks
    private SupplierService supplierService;

    @Test
    void addSupplier_shouldSaveSupplierToRepository() {
        // Arrange
        SupplierDTO dto = new SupplierDTO("PT Maju", "Bandung", "08123456789", "Elektronik");

        // Act
        supplierService.addSupplier(dto);

        // Assert
        ArgumentCaptor<Supplier> captor = ArgumentCaptor.forClass(Supplier.class);
        verify(supplierRepository, times(1)).save(captor.capture());

        Supplier savedSupplier = captor.getValue();

        assertEquals("PT Maju", savedSupplier.getName());
        assertEquals("Bandung", savedSupplier.getAddress());
        assertEquals("08123456789", savedSupplier.getContact());
        assertEquals("Elektronik", savedSupplier.getCategory());
    }

     */
}
