package id.ac.ui.cs.advprog.buildingstore.supplier.factory;

import id.ac.ui.cs.advprog.buildingstore.supplier.dto.PurchaseTransactionDTO;
import id.ac.ui.cs.advprog.buildingstore.supplier.model.PurchaseTransaction;

public class PurchaseTransactionFactory {

    public static PurchaseTransaction fromDTO(PurchaseTransactionDTO dto) {
        if (dto == null || dto.getSupplier() == null) {
            throw new IllegalArgumentException("Supplier must not be null");
        }

        return PurchaseTransaction.builder()
                .supplier(dto.getSupplier())
                .productName(dto.getProductName())
                .quantity(dto.getQuantity())
                .totalPrice(dto.getTotalPrice())
                .date(dto.getDate())
                .build();
    }
}
