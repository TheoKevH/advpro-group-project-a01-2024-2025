package id.ac.ui.cs.advprog.buildingstore.supplier.service;

import id.ac.ui.cs.advprog.buildingstore.product.dto.ProductRequestDTO;
import id.ac.ui.cs.advprog.buildingstore.supplier.dto.PurchaseTransactionDTO;
import id.ac.ui.cs.advprog.buildingstore.supplier.model.PurchaseTransaction;
import id.ac.ui.cs.advprog.buildingstore.supplier.factory.PurchaseTransactionFactory;
import id.ac.ui.cs.advprog.buildingstore.supplier.model.Supplier;
import id.ac.ui.cs.advprog.buildingstore.supplier.repository.PurchaseTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class PurchaseTransactionServiceImpl implements PurchaseTransactionService {

    private final PurchaseTransactionRepository transactionRepo;
    private final RestTemplate restTemplate;

    @Override
    public void addTransaction(PurchaseTransactionDTO dto) {
        PurchaseTransaction transaction = PurchaseTransactionFactory.fromDTO(dto);
        transactionRepo.save(transaction);

        log.info("Saved transaction: supplierId={}, product={}, qty={}",
                dto.getSupplier().getId(), dto.getProductName(), dto.getQuantity());

        ProductRequestDTO productRequest = new ProductRequestDTO();
        productRequest.setProductName(dto.getProductName());
        productRequest.setProductQuantity(dto.getQuantity());

        String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/product/insert")
                .toUriString();

        try {
            restTemplate.postForEntity(url, productRequest, Void.class);
            log.info("Synced product stock via POST to {} for product={}", url, dto.getProductName());
        } catch (Exception e) {
            log.warn("Failed to sync product stock for product={} due to {}", dto.getProductName(), e.getMessage());
        }
    }

    @Override
    public List<PurchaseTransaction> getTransactionsBySupplier(Supplier supplier) {
        List<PurchaseTransaction> list = transactionRepo.findBySupplier(supplier);
        log.info("Fetched {} transactions for supplierId={}", list.size(), supplier.getId());
        return list;
    }

    @Override
    @Async
    public CompletableFuture<List<PurchaseTransaction>> getTransactionsBySupplierAsync(Supplier supplier) {
        List<PurchaseTransaction> list = transactionRepo.findBySupplier(supplier);
        log.info("Async fetch: {} transactions for supplierId={}", list.size(), supplier.getId());
        return CompletableFuture.completedFuture(list);
    }
}
