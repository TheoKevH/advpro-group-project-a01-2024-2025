package id.ac.ui.cs.advprog.buildingstore.transaksi.model;

import id.ac.ui.cs.advprog.buildingstore.transaksi.enums.TransactionStatus;

public class InProgressState implements TransactionState {
    @Override
    public void moveToPayment(Transaction transaction) {
        transaction.setState(new AwaitingPaymentState());
        transaction.setStatus(TransactionStatus.AWAITING_PAYMENT);

    }

    @Override
    public void cancel(Transaction transaction) {

        transaction.setState(new CancelledState());
        transaction.setStatus(TransactionStatus.CANCELLED);
    }

    @Override
    public void markAsPaid(Transaction transaction) {
        throw new IllegalStateException("Cannot mark as paid from IN_PROGRESS. Move to payment first.");
    }

    @Override
    public TransactionStatus getStatus() {
        return TransactionStatus.IN_PROGRESS;
    }
}
