package id.ac.ui.cs.advprog.buildingstore.transaksi.model;


import id.ac.ui.cs.advprog.buildingstore.transaksi.enums.TransactionStatus;

public class CompletedState implements TransactionState{
    @Override
    public void moveToPayment(Transaction transaction) {
        throw new IllegalStateException("Transaction is already completed.");
    }

    @Override
    public void cancel(Transaction transaction) {
        throw new IllegalStateException("Cannot cancel a completed transaction.");
    }

    @Override
    public void markAsPaid(Transaction transaction) {
        throw new IllegalStateException("Already marked as completed.");
    }

    @Override
    public TransactionStatus getStatus() {
        return TransactionStatus.COMPLETED;
    }
}
