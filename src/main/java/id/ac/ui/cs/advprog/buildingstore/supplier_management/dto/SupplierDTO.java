package id.ac.ui.cs.advprog.buildingstore.supplier_management.dto;
import id.ac.ui.cs.advprog.buildingstore.supplier_management.model.Supplier;
import id.ac.ui.cs.advprog.buildingstore.supplier_management.model.SupplierCategory;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierDTO {
    private String name;
    private String address;
    private String contact;
    private SupplierCategory category;

    public static SupplierDTO fromEntity(Supplier supplier) {
        return new SupplierDTO(
                supplier.getName(),
                supplier.getAddress(),
                supplier.getContact(),
                supplier.getCategory()
        );
    }
}