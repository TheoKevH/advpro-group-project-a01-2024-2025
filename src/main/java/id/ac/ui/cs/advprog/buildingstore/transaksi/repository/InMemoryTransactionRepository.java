package id.ac.ui.cs.advprog.buildingstore.transaksi.repository;

import id.ac.ui.cs.advprog.buildingstore.transaksi.model.Transaction;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InMemoryTransactionRepository implements TransactionRepository {

    private final Map<String, Transaction> transactionMap = new HashMap<>();

    @Override
    public Transaction save(Transaction transaction) {
        transactionMap.put(transaction.getId(), transaction);
        return transaction;
    }

    @Override
    public Transaction findById(String id) {
        return transactionMap.get(id);
    }

    @Override
    public List<Transaction> findAll() {
        return new ArrayList<>(transactionMap.values());
    }

    @Override
    public void delete(String id) {
        transactionMap.remove(id);
    }
}
