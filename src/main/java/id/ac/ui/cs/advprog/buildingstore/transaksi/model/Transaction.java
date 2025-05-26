package id.ac.ui.cs.advprog.buildingstore.transaksi.model;

import id.ac.ui.cs.advprog.buildingstore.authentication.model.User;
import id.ac.ui.cs.advprog.buildingstore.transaksi.enums.TransactionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "transaction")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Builder.Default
    @Id
    @Column(name = "transaction_id")
    private String transactionId = UUID.randomUUID().toString();

    private String customerId;

    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User createdBy;


    @Builder.Default
    @Transient
    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionItem> items = new ArrayList<>();

    @Builder.Default
    @Transient
    private TransactionState state = new InProgressState();

    // Using State Pattern because the transactions will have different statuses that behaves differently

    public void moveToPayment() {
        state.moveToPayment(this);
    }

    public void cancel() {
        state.cancel(this);
    }

    public void markAsPaid() {
        state.markAsPaid(this);
    }

    public TransactionStatus getStatus() {
        return state.getStatus();
    }

    public boolean isEditable() {
        return state.getStatus() == TransactionStatus.IN_PROGRESS;
    }
}
