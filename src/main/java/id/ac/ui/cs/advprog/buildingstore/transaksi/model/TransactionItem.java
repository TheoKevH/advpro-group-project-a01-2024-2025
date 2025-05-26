package id.ac.ui.cs.advprog.buildingstore.transaksi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TransactionItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)

    private String id;
    private String productId;
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;
}
