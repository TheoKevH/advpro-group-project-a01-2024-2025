package id.ac.ui.cs.advprog.buildingstore.transaksi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionItem {
    private String productId;
    private int quantity;
}
