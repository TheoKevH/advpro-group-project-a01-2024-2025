package id.ac.ui.cs.advprog.buildingstore.supplier_management.service;

import id.ac.ui.cs.advprog.buildingstore.supplier_management.dto.PurchaseTransactionDTO;
import id.ac.ui.cs.advprog.buildingstore.supplier_management.model.PurchaseTransaction;
import id.ac.ui.cs.advprog.buildingstore.supplier_management.factory.PurchaseTransactionFactory;
import id.ac.ui.cs.advprog.buildingstore.supplier_management.model.Supplier;
import id.ac.ui.cs.advprog.buildingstore.supplier_management.repository.PurchaseTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseTransactionServiceImpl implements PurchaseTransactionService {

    private final PurchaseTransactionRepository transactionRepo;

    @Override
    public void addTransaction(PurchaseTransactionDTO dto) {
        PurchaseTransaction transaction = PurchaseTransactionFactory.fromDTO(dto);
        transactionRepo.save(transaction);
    }

    @Override
    public List<PurchaseTransaction> getTransactionsBySupplier(Supplier supplier) {
        return transactionRepo.findBySupplier(supplier);
    }
}
