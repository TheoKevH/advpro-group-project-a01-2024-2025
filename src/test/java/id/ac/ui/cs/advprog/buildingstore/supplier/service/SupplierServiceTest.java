package id.ac.ui.cs.advprog.buildingstore.supplier.service;

import id.ac.ui.cs.advprog.buildingstore.supplier.dto.SupplierDTO;
import id.ac.ui.cs.advprog.buildingstore.supplier.model.Supplier;
import id.ac.ui.cs.advprog.buildingstore.supplier.model.SupplierCategory;
import id.ac.ui.cs.advprog.buildingstore.supplier.repository.SupplierRepository;
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

    private SupplierServiceImpl supplierService;

    @BeforeEach
    void setUp() {
        supplierService = new SupplierServiceImpl(repo);
    }

    @Test
    void addSupplier_shouldSaveSupplierToRepository() {
        SupplierDTO dto = new SupplierDTO("PT Maju", "Bandung", "08123456789", SupplierCategory.LISTRIK);

        supplierService.addSupplier(dto);

        ArgumentCaptor<Supplier> captor = ArgumentCaptor.forClass(Supplier.class);
        verify(repo, times(1)).save(captor.capture());

        Supplier savedSupplier = captor.getValue();

        assertEquals("[LISTRIK] PT Maju", savedSupplier.getName());
        assertEquals("Bandung", savedSupplier.getAddress());
        assertEquals("08123456789", savedSupplier.getContact());
        assertEquals(SupplierCategory.LISTRIK, savedSupplier.getCategory());
    }

    @Test
    void addSupplier_shouldThrowIfDtoIsNull() {
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                supplierService.addSupplier(null)
        );
        assertEquals("SupplierDTO cannot be null", ex.getMessage());
    }


    @Test
    void editSupplier_shouldUpdateAndSaveCorrectly() {
        Long supplierId = 1L;
        Supplier existingSupplier = Supplier.builder()
                .id(supplierId)
                .name("PT Lama")
                .address("Jakarta")
                .contact("0811111111")
                .category(SupplierCategory.KAYU)
                .build();

        SupplierDTO updatedDTO = new SupplierDTO(
                "PT Baru",
                "Bandung",
                "0822222222",
                SupplierCategory.LISTRIK
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
        assertEquals(SupplierCategory.LISTRIK, updated.getCategory());
    }

    @Test
    void editSupplier_shouldThrowIfIdIsNull() {
        SupplierDTO dto = new SupplierDTO("X", "Y", "Z", SupplierCategory.KAYU);
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                supplierService.editSupplier(null, dto)
        );
        assertEquals("Supplier ID cannot be null", ex.getMessage());
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

    @Test
    void findById_shouldReturnSupplier() {
        Long id = 1L;
        Supplier supplier = Supplier.builder().id(id).name("Supplier X").build();
        when(repo.findById(id)).thenReturn(Optional.of(supplier));

        Supplier result = supplierService.findById(id);

        assertNotNull(result);
        assertEquals("Supplier X", result.getName());
    }

    @Test
    void findById_shouldThrowIfNotFound() {
        Long id = 42L;
        when(repo.findById(id)).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                supplierService.findById(id)
        );

        assertEquals("Supplier with id 42 not found", ex.getMessage());
    }

    @Test
    void findById_shouldThrowIfIdIsNull() {
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                supplierService.findById(null)
        );
        assertEquals("Supplier ID cannot be null", ex.getMessage());
    }

}
