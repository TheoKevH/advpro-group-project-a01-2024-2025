package id.ac.ui.cs.advprog.buildingstore.product.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private String productId;

    @NotNull(message = "Nama produk tidak boleh kosong")
    private String productName;

    @NotNull(message = "Harga tidak boleh kosong")
    @DecimalMin(value = "0.00", inclusive = true, message = "Harga tidak boleh negatif")
    private BigDecimal productPrice;

    @NotNull(message = "Quantity tidak boleh kosong")
    @Min(value = 0, message = "Quantity tidak boleh negatif")
    private int productQuantity;
    private String productDescription;
}
