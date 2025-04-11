package id.ac.ui.cs.advprog.buildingstore.transaksi.model;

import id.ac.ui.cs.advprog.buildingstore.transaksi.enums.TransactionStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Transaction {

    private String id;
    private List<TransactionItem> items = new ArrayList<>();
    private TransactionState state;

    public Transaction() {
        this.id = UUID.randomUUID().toString();
        this.state = new InProgressState();
    }

    // State pattern delegations

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

    public void setState(TransactionState state) {
        this.state = state;
    }

    public boolean isEditable() {
        return state.getStatus() == TransactionStatus.IN_PROGRESS;
    }
}
