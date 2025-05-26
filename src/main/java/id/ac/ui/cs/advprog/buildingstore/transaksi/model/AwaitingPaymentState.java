package id.ac.ui.cs.advprog.buildingstore.transaksi.model;

import id.ac.ui.cs.advprog.buildingstore.transaksi.enums.TransactionStatus;

public class AwaitingPaymentState implements TransactionState {
    @Override
    public void moveToPayment(Transaction transaction) {
        throw new IllegalStateException("Already awaiting payment.");
    }

    @Override
    public void cancel(Transaction transaction) {
        throw new IllegalStateException("Cannot cancel while awaiting payment.");
    }

    @Override
    public void markAsPaid(Transaction transaction) {
        transaction.setState(new CompletedState());
        transaction.setStatus(TransactionStatus.COMPLETED);
    }

    @Override
    public TransactionStatus getStatus() {
        return TransactionStatus.AWAITING_PAYMENT;
    }
}
