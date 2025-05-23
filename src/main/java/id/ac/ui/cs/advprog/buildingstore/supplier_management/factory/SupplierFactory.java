package id.ac.ui.cs.advprog.buildingstore.supplier_management.factory;

import id.ac.ui.cs.advprog.buildingstore.supplier_management.dto.SupplierDTO;
import id.ac.ui.cs.advprog.buildingstore.supplier_management.model.Supplier;

public class SupplierFactory {

    public static Supplier fromDTO(SupplierDTO dto) {
        if (dto == null || dto.getCategory() == null) {
            throw new IllegalArgumentException("Supplier category cannot be null");
        }

        return switch (dto.getCategory()) {
            case SEMEN -> buildSemenSupplier(dto);
            case KAYU -> buildKayuSupplier(dto);
            case BESI_BAJA -> buildBesiSupplier(dto);
            case LISTRIK -> buildListrikSupplier(dto);
            case PLUMBING -> buildPlumbingSupplier(dto);
            case ATAP_DINDING -> buildAtapSupplier(dto);
        };
    }

    private static Supplier buildSemenSupplier(SupplierDTO dto) {
        return base(dto).name("[SEMEN] " + dto.getName()).build();
    }

    private static Supplier buildKayuSupplier(SupplierDTO dto) {
        return base(dto).name("[KAYU] " + dto.getName()).build();
    }

    private static Supplier buildBesiSupplier(SupplierDTO dto) {
        return base(dto).name("[BESI_BAJA] " + dto.getName()).build();
    }

    private static Supplier buildListrikSupplier(SupplierDTO dto) {
        return base(dto).name("[LISTRIK] " + dto.getName()).build();
    }

    private static Supplier buildPlumbingSupplier(SupplierDTO dto) {
        return base(dto).name("[PLUMBING] " + dto.getName()).build();
    }

    private static Supplier buildAtapSupplier(SupplierDTO dto) {
        return base(dto).name("[ATAP_DINDING] " + dto.getName()).build();
    }

    private static Supplier.SupplierBuilder base(SupplierDTO dto) {
        return Supplier.builder()
                .address(dto.getAddress())
                .contact(dto.getContact())
                .category(dto.getCategory());
    }
}
