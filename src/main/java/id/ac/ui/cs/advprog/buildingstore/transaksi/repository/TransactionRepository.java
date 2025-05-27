package id.ac.ui.cs.advprog.buildingstore.transaksi.repository;

import id.ac.ui.cs.advprog.buildingstore.transaksi.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, String>{

}
