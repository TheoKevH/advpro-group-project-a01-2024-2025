package id.ac.ui.cs.advprog.buildingstore.supplier.service;

import id.ac.ui.cs.advprog.buildingstore.product.dto.ProductRequestDTO;
import id.ac.ui.cs.advprog.buildingstore.supplier.dto.PurchaseTransactionDTO;
import id.ac.ui.cs.advprog.buildingstore.supplier.model.PurchaseTransaction;
import id.ac.ui.cs.advprog.buildingstore.supplier.factory.PurchaseTransactionFactory;
import id.ac.ui.cs.advprog.buildingstore.supplier.model.Supplier;
import id.ac.ui.cs.advprog.buildingstore.supplier.repository.PurchaseTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class PurchaseTransactionServiceImpl implements PurchaseTransactionService {

    private final PurchaseTransactionRepository transactionRepo;
    private final RestTemplate restTemplate;

    @Override
    public void addTransaction(PurchaseTransactionDTO dto) {
        PurchaseTransaction transaction = PurchaseTransactionFactory.fromDTO(dto);
        transactionRepo.save(transaction);

        ProductRequestDTO productRequest = new ProductRequestDTO();
        productRequest.setProductName(dto.getProductName());
        productRequest.setProductQuantity(dto.getQuantity());

        String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/product/insert")
                .toUriString();
        restTemplate.postForEntity(url, productRequest, Void.class);
    }

    @Override
    public List<PurchaseTransaction> getTransactionsBySupplier(Supplier supplier) {
        return transactionRepo.findBySupplier(supplier);
    }

    @Override
    @Async
    public CompletableFuture<List<PurchaseTransaction>> getTransactionsBySupplierAsync(Supplier supplier) {
        List<PurchaseTransaction> list = transactionRepo.findBySupplier(supplier);
        return CompletableFuture.completedFuture(list);
    }
}
