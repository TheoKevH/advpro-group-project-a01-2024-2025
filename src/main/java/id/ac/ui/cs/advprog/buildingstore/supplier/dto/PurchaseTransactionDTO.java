package id.ac.ui.cs.advprog.buildingstore.supplier.dto;

import id.ac.ui.cs.advprog.buildingstore.supplier.model.Supplier;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseTransactionDTO {
    @NotBlank(message = "Product name is required")
    @Size(max = 100)
    private String productName;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    @NotNull(message = "Total price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Total price must be greater than 0")
    private BigDecimal totalPrice;

    @NotNull(message = "Date is required")
    private LocalDateTime date;

    @NotNull
    private Supplier supplier;
}
