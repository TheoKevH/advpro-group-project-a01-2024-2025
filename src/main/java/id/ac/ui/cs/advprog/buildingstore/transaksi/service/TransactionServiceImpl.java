package id.ac.ui.cs.advprog.buildingstore.transaksi.service;

import id.ac.ui.cs.advprog.buildingstore.transaksi.model.Transaction;
import id.ac.ui.cs.advprog.buildingstore.transaksi.model.TransactionItem;
import id.ac.ui.cs.advprog.buildingstore.transaksi.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import id.ac.ui.cs.advprog.buildingstore.authentication.repository.UserRepository;
import id.ac.ui.cs.advprog.buildingstore.authentication.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private AsyncTransactionLogger asyncTransactionLogger;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public Transaction createTransaction(String customerId, List<TransactionItem> items) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User creator = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Transaction transaction = Transaction.builder().
                customerId(customerId).
                items(items).
                createdBy(creator).
                build();

        for (TransactionItem item : items) {
            item.setTransaction(transaction); // semoga bisaaa woiiii!!!!
        }

        System.out.println("Creating transaction for customer: " + customerId);
        System.out.println("Items: " + items.size());

        transaction.setItems(items);

        return repository.save(transaction);
    }

    @Override
    public Transaction getTransaction(String id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return repository.findAll();
    }

    @Override
    public Transaction moveToPayment(String id) {
        Transaction trx = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
        trx.moveToPayment();
        Transaction saved = repository.save(trx);
        asyncTransactionLogger.logTransactionStatus(saved);
        return saved;
    }

    @Override
    public Transaction markAsPaid(String id) {
        Transaction trx = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
        trx.markAsPaid();
        Transaction saved = repository.save(trx);
        asyncTransactionLogger.logTransactionStatus(saved);
        return saved;
    }

    @Override
    public void cancelTransaction(String id) {
        Transaction trx = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
        trx.cancel();
        repository.save(trx);
        asyncTransactionLogger.logTransactionStatus(trx);
    }

    @Override
    public Transaction updateTransaction(String id, List<TransactionItem> items) {
        Transaction trx = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
        if (!trx.isEditable()) {
            throw new IllegalStateException("Transaksi tidak bisa diedit karena sudah bukan IN_PROGRESS");
        }
        trx.setItems(items);
        return repository.save(trx);
    }

    @Override
    public List<Transaction> getTransactionsByCustomer(String customerId) {
        return repository.findAll().stream()
                .filter(trx -> trx.getCustomerId() != null && trx.getCustomerId().equals(customerId))
                .toList();
    }

    @Override
    public List<Transaction> getTransactionsByUser(User user) {
        return repository.findAll().stream()
                .filter(trx -> trx.getCreatedBy() != null && trx.getCreatedBy().getId().equals(user.getId()))
                .toList();
    }


}
