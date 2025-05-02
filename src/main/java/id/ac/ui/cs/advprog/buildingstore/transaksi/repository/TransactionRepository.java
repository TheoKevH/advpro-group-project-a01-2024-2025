package id.ac.ui.cs.advprog.buildingstore.transaksi.repository;

import id.ac.ui.cs.advprog.buildingstore.transaksi.model.Transaction;

import java.util.List;

public interface TransactionRepository {
    Transaction save(Transaction transaction);
    Transaction findById(String id);
    List<Transaction> findAll();
    void delete(String id);
}
