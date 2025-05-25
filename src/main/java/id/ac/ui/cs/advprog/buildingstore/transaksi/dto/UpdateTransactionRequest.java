package id.ac.ui.cs.advprog.buildingstore.transaksi.dto;

import id.ac.ui.cs.advprog.buildingstore.transaksi.model.TransactionItem;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateTransactionRequest {
    private List<TransactionItem> items;
}
