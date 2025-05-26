package id.ac.ui.cs.advprog.buildingstore.transaksi.controller;

import id.ac.ui.cs.advprog.buildingstore.authentication.model.User;
import id.ac.ui.cs.advprog.buildingstore.authentication.repository.UserRepository;
import id.ac.ui.cs.advprog.buildingstore.transaksi.dto.CreateTransactionRequest;
import id.ac.ui.cs.advprog.buildingstore.transaksi.dto.UpdateTransactionRequest;
import id.ac.ui.cs.advprog.buildingstore.transaksi.model.Transaction;
import id.ac.ui.cs.advprog.buildingstore.transaksi.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService service;
    private final UserRepository userRepository;

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


}
