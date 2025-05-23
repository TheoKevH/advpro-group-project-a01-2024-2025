package id.ac.ui.cs.advprog.buildingstore.transaksi.service;

import id.ac.ui.cs.advprog.buildingstore.transaksi.model.Transaction;
import id.ac.ui.cs.advprog.buildingstore.transaksi.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository repository;


    @Override
    public Transaction createTransaction() {
        Transaction transaction = new Transaction();
        return repository.save(transaction);
    }

    @Override
    public Transaction getTransaction(String id) {
        return repository.findById(id);
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return repository.findAll();
    }

    @Override
    public Transaction moveToPayment(String id) {
        Transaction trx = repository.findById(id);
        trx.moveToPayment();
        return repository.save(trx);
    }

    @Override
    public Transaction markAsPaid(String id) {
        Transaction trx = repository.findById(id);
        trx.markAsPaid();
        return repository.save(trx);
    }

    @Override
    public void cancelTransaction(String id) {
        Transaction trx = repository.findById(id);
        trx.cancel();
        repository.save(trx);
    }
}
