package id.ac.ui.cs.advprog.buildingstore.transaksi.model;

import id.ac.ui.cs.advprog.buildingstore.transaksi.enums.TransactionStatus;

public interface TransactionState {
    void moveToPayment(Transaction transaction);
    void cancel(Transaction transaction);
    void markAsPaid(Transaction transaction);
    TransactionStatus getStatus();
}
