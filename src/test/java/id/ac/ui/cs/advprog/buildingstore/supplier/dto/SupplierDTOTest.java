package id.ac.ui.cs.advprog.buildingstore.supplier.dto;

import id.ac.ui.cs.advprog.buildingstore.supplier.model.Supplier;
import id.ac.ui.cs.advprog.buildingstore.supplier.model.SupplierCategory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SupplierDTOTest {

    @Test
    void canCreateSupplierDTOAndAccessFields() {
        SupplierDTO dto = new SupplierDTO(
                "PT Sumber Makmur",
                "Jl. Makmur No. 1",
                "081234567890",
                SupplierCategory.LISTRIK
        );

        assertThat(dto.getName()).isEqualTo("PT Sumber Makmur");
        assertThat(dto.getAddress()).isEqualTo("Jl. Makmur No. 1");
        assertThat(dto.getContact()).isEqualTo("081234567890");
        assertThat(dto.getCategory()).isEqualTo(SupplierCategory.LISTRIK);
    }

    @Test
    void canConvertFromEntity() {
        Supplier supplier = Supplier.builder()
                .name("PT ABC")
                .address("Jakarta")
                .contact("0812121212")
                .category(SupplierCategory.SEMEN)
                .build();

        SupplierDTO dto = SupplierDTO.fromEntity(supplier);

        assertThat(dto.getName()).isEqualTo("PT ABC");
        assertThat(dto.getAddress()).isEqualTo("Jakarta");
        assertThat(dto.getContact()).isEqualTo("0812121212");
        assertThat(dto.getCategory()).isEqualTo(SupplierCategory.SEMEN);
    }
}
