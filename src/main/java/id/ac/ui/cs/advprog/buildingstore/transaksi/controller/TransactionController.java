package id.ac.ui.cs.advprog.buildingstore.transaksi.controller;

import id.ac.ui.cs.advprog.buildingstore.authentication.model.User;
import id.ac.ui.cs.advprog.buildingstore.authentication.repository.UserRepository;
import id.ac.ui.cs.advprog.buildingstore.transaksi.dto.*;
import id.ac.ui.cs.advprog.buildingstore.transaksi.model.Transaction;
import id.ac.ui.cs.advprog.buildingstore.transaksi.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService service;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Transaction> createTransaction(@RequestBody CreateTransactionRequest request) {
        Transaction trx = service.createTransaction(request.getCustomerId(), request.getItems());
        return ResponseEntity.ok(trx);
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String createTransactionFromForm(@ModelAttribute CreateTransactionRequest request) {
        service.createTransaction(request.getCustomerId(), request.getItems());
        return "redirect:/transaksi";
    }


    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(service.getAllTransactions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable String id) {
        Transaction trx = service.getTransaction(id);
        if (trx == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(trx);
    }

    @PutMapping("/{id}/payment")
    public ResponseEntity<?> moveToPayment(@PathVariable String id) {
        try {
            Transaction trx = service.moveToPayment(id);
            return ResponseEntity.ok(trx);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/pay")
    public ResponseEntity<?> markAsPaid(@PathVariable String id) {
        try {
            Transaction trx = service.markAsPaid(id);
            return ResponseEntity.ok(trx);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelTransaction(@PathVariable String id) {
        try {
            service.cancelTransaction(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTransaction(
            @PathVariable String id,
            @RequestBody UpdateTransactionRequest request
    ) {
        try {
            Transaction updated = service.updateTransaction(id, request.getItems());
            return ResponseEntity.ok(updated);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Transaction>> getByCustomer(@PathVariable String customerId) {
        List<Transaction> trxList = service.getTransactionsByCustomer(customerId);
        return ResponseEntity.ok(trxList);
    }

    @GetMapping("/my-transactions")
    public ResponseEntity<List<Transaction>> getMyTransactions() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return ResponseEntity.ok(service.getTransactionsByUser(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/created-by/{username}")
    public ResponseEntity<List<Transaction>> getTransactionsByCreator(@PathVariable String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return ResponseEntity.ok(service.getTransactionsByUser(user));
    }

    @GetMapping("/{id}/view")
    public ResponseEntity<Map<String, Object>> getTransactionView(@PathVariable String id) {
        Transaction transaction = service.getTransaction(id);

        String productUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/product")
                .toUriString();
        ProductDTO[] allProducts = restTemplate.getForObject(productUrl, ProductDTO[].class);
        Map<String, ProductDTO> productMap = new HashMap<>();
        for (ProductDTO p : allProducts) productMap.put(p.getProductId(), p);

        String customerUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/customers/" + transaction.getCustomerId())
                .toUriString();
        CustomerDTO customer = restTemplate.getForObject(customerUrl, CustomerDTO.class);

        List<Map<String, Object>> itemViews = transaction.getItems().stream().map(item -> {
            Map<String, Object> m = new HashMap<>();
            ProductDTO prod = productMap.get(item.getProductId());
            m.put("productName", prod != null ? prod.getProductName() : "Unknown");
            m.put("productPrice", prod != null ? prod.getProductPrice() : 0);
            m.put("quantity", item.getQuantity());
            return m;
        }).toList();

        Map<String, Object> response = new HashMap<>();
        response.put("customerName", customer != null ? customer.getName() : "Unknown");
        response.put("items", itemViews);

        return ResponseEntity.ok(response);
    }

}
