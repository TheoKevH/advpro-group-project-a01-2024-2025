package id.ac.ui.cs.advprog.buildingstore.supplier_management.factory;

import id.ac.ui.cs.advprog.buildingstore.supplier_management.dto.SupplierDTO;
import id.ac.ui.cs.advprog.buildingstore.supplier_management.model.Supplier;

public class SupplierFactory {
    public static Supplier fromDTO(SupplierDTO dto) {
        return Supplier.builder()
                .name(dto.getName())
                .address(dto.getAddress())
                .contact(dto.getContact())
                .category(dto.getCategory())
                .build();
    }
}