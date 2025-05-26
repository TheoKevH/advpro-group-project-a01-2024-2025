package id.ac.ui.cs.advprog.buildingstore.supplier.dto;
import id.ac.ui.cs.advprog.buildingstore.supplier.model.Supplier;
import id.ac.ui.cs.advprog.buildingstore.supplier.model.SupplierCategory;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierDTO {
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be at most 100 characters")
    private String name;

    @NotBlank(message = "Address is required")
    @Size(max = 255, message = "Address must be at most 255 characters")
    private String address;

    @NotBlank(message = "Contact is required")
    @Size(max = 50, message = "Contact must be at most 50 characters")
    @Pattern(regexp = "^[0-9+()\\s-]+$", message = "Contact must be a valid phone number")
    private String contact;

    @NotNull(message = "Category is required")
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