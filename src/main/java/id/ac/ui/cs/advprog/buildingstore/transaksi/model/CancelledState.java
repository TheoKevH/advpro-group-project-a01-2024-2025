package id.ac.ui.cs.advprog.buildingstore.transaksi.model;

import id.ac.ui.cs.advprog.buildingstore.transaksi.enums.TransactionStatus;

public class CancelledState implements TransactionState {
    @Override
    public void moveToPayment(Transaction transaction) {
        throw new IllegalStateException("Cannot move to payment. Transaction is cancelled.");
    }

    @Override
    public void cancel(Transaction transaction) {
        throw new IllegalStateException("Transaction already cancelled.");
    }

    @Override
    public void markAsPaid(Transaction transaction) {
        throw new IllegalStateException("Cannot complete a cancelled transaction.");
    }

    @Override
    public TransactionStatus getStatus() {
        return TransactionStatus.CANCELLED;
    }
}
