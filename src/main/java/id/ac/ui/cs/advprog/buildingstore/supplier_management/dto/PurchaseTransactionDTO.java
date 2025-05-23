package id.ac.ui.cs.advprog.buildingstore.supplier_management.dto;

import id.ac.ui.cs.advprog.buildingstore.supplier_management.model.Supplier;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseTransactionDTO {
    private Supplier supplier;
    private String productName;
    private int quantity;
    private BigDecimal totalPrice;
    private LocalDateTime date;
}
