package id.ac.ui.cs.advprog.buildingstore.transaksi.service;

import id.ac.ui.cs.advprog.buildingstore.transaksi.model.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction getTransaction(String id);
    List<Transaction> getAllTransactions();
    Transaction moveToPayment(String id);
    Transaction markAsPaid(String id);
    void cancelTransaction(String id);
    Transaction createTransaction(String customerId, List<TransactionItem> items);
}
