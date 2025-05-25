package id.ac.ui.cs.advprog.buildingstore.transaksi.model;

import id.ac.ui.cs.advprog.buildingstore.transaksi.enums.TransactionStatus;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Builder.Default
    private String transactionId = UUID.randomUUID().toString();

    private String customerId;

    @Builder.Default
    private List<TransactionItem> items = new ArrayList<>();

    @Builder.Default
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
