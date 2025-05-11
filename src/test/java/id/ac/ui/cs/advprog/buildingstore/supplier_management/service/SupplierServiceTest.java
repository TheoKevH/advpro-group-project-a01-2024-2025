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
import java.util.List;
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

    @Test
    void deleteSupplier_shouldCallRepositoryDeleteById() {
        Long id = 1L;
        when(repo.existsById(id)).thenReturn(true);

        supplierService.deleteSupplier(id);

        verify(repo).deleteById(id);
    }

    @Test
    void deleteSupplier_shouldThrowExceptionIfSupplierNotFound() {
        Long id = 99L;
        when(repo.existsById(id)).thenReturn(false);

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                supplierService.deleteSupplier(id)
        );

        assertEquals("Supplier with id 99 not found", ex.getMessage());
    }

    @Test
    void getAllSuppliers_shouldReturnListOfSuppliers() {
        List<Supplier> mockSuppliers = List.of(
                Supplier.builder().id(1L).name("Supplier A").build(),
                Supplier.builder().id(2L).name("Supplier B").build()
        );

        when(repo.findAll()).thenReturn(mockSuppliers);

        List<Supplier> result = supplierService.getAllSuppliers();

        assertEquals(2, result.size());
        assertEquals("Supplier A", result.get(0).getName());
        assertEquals("Supplier B", result.get(1).getName());
    }




}
