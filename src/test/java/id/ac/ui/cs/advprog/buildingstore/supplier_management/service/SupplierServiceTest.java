package id.ac.ui.cs.advprog.buildingstore.supplier_management.service;

import id.ac.ui.cs.advprog.buildingstore.supplier_management.dto.SupplierDTO;
import id.ac.ui.cs.advprog.buildingstore.supplier_management.model.Supplier;
import id.ac.ui.cs.advprog.buildingstore.supplier_management.repository.SupplierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SupplierServiceTest {

    @Mock
    private SupplierRepository repo;

    private SupplierService supplierService;

    @BeforeEach
    void setUp() {
        supplierService = new SupplierServiceImpl(repo);
    }

    @Test
    void addSupplier_shouldSaveSupplierToRepository() {
        SupplierDTO dto = new SupplierDTO("PT Maju", "Bandung", "08123456789", "Elektronik");

        supplierService.addSupplier(dto);

        ArgumentCaptor<Supplier> captor = ArgumentCaptor.forClass(Supplier.class);
        verify(repo, times(1)).save(captor.capture());

        Supplier savedSupplier = captor.getValue();

        assertEquals("PT Maju", savedSupplier.getName());
        assertEquals("Bandung", savedSupplier.getAddress());
        assertEquals("08123456789", savedSupplier.getContact());
        assertEquals("Elektronik", savedSupplier.getCategory());
    }

    @Test
    void editSupplier_shouldUpdateAndSaveCorrectly() {
        Long supplierId = 1L;
        Supplier existingSupplier = Supplier.builder()
                .id(supplierId)
                .name("PT Lama")
                .address("Jakarta")
                .contact("0811111111")
                .category("Lama")
                .build();

        SupplierDTO updatedDTO = new SupplierDTO(
                "PT Baru",
                "Bandung",
                "0822222222",
                "Elektronik"
        );

        when(repo.findById(supplierId)).thenReturn(Optional.of(existingSupplier));

        supplierService.editSupplier(supplierId, updatedDTO);

        ArgumentCaptor<Supplier> captor = ArgumentCaptor.forClass(Supplier.class);
        verify(repo).save(captor.capture());

        Supplier updated = captor.getValue();
        assertEquals(supplierId, updated.getId());
        assertEquals("PT Baru", updated.getName());
        assertEquals("Bandung", updated.getAddress());
        assertEquals("0822222222", updated.getContact());
        assertEquals("Elektronik", updated.getCategory());
    }

}
