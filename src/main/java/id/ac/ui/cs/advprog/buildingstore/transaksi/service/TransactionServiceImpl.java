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

    @Autowired
    private AsyncTransactionLogger asyncTransactionLogger;


    @Override
    public Transaction createTransaction(String customerId, List<TransactionItem> items) {
        Transaction transaction = Transaction.builder().
                customerId(customerId).
                items(items).
                build();

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
        Transaction saved = repository.save(trx);
        asyncTransactionLogger.logTransactionStatus(saved);
        return saved;
    }

    @Override
    public Transaction markAsPaid(String id) {
        Transaction trx = repository.findById(id);
        trx.markAsPaid();
        Transaction saved = repository.save(trx);
        asyncTransactionLogger.logTransactionStatus(saved);
        return saved;
    }

    @Override
    public void cancelTransaction(String id) {
        Transaction trx = repository.findById(id);
        trx.cancel();
        repository.save(trx);
        asyncTransactionLogger.logTransactionStatus(trx);
    }
}
