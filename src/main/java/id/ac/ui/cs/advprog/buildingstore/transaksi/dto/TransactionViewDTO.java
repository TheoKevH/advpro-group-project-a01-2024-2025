package id.ac.ui.cs.advprog.buildingstore.transaksi.dto;

import lombok.Data;

import java.util.List;

@Data
public class TransactionViewDTO {
    private String transactionId;
    private String customerId;
    private String status;
    private List<TransactionItemDTO> items;

    @Data
    public static class TransactionItemDTO {
        private String productId;
        private String productName;
        private int quantity;
        private int productPrice;
    }
}
