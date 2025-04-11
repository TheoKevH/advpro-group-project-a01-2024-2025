package id.ac.ui.cs.advprog.buildingstore.supplier_management.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierDTO {
    private String name;
    private String address;
    private String contact;
    private String category;
}