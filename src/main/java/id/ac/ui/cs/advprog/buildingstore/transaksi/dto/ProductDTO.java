package id.ac.ui.cs.advprog.buildingstore.transaksi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO {
    private String productId;
    private String productName;
    private String productDescription;
    private int productQuantity;
    private int productPrice;

}
