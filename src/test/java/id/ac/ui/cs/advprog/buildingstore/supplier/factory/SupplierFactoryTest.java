package id.ac.ui.cs.advprog.buildingstore.supplier.factory;

import id.ac.ui.cs.advprog.buildingstore.supplier.dto.SupplierDTO;
import id.ac.ui.cs.advprog.buildingstore.supplier.model.Supplier;
import id.ac.ui.cs.advprog.buildingstore.supplier.model.SupplierCategory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class SupplierFactoryTest {
    @Test
    void createSupplier_shouldPrefixNameBasedOnCategory() {
        for (SupplierCategory category : SupplierCategory.values()) {
            SupplierDTO dto = new SupplierDTO("Nama", "Alamat", "Kontak", category);
            Supplier supplier = SupplierFactory.fromDTO(dto);

            String expectedPrefix = "[" + category.name() + "]";
            assertThat(supplier.getName()).startsWith(expectedPrefix);
            assertThat(supplier.getCategory()).isEqualTo(category);
        }
    }

    @Test
    void createSupplier_shouldThrowIfCategoryIsNull() {
        SupplierDTO dto = new SupplierDTO("Nama", "Alamat", "Kontak", null);
        assertThatThrownBy(() -> SupplierFactory.fromDTO(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("category");
    }

    @Test
    void createSupplier_shouldThrowIfDtoIsNull() {
        assertThatThrownBy(() -> SupplierFactory.fromDTO(null))
                .isInstanceOf(IllegalArgumentException.class);
    }


}
