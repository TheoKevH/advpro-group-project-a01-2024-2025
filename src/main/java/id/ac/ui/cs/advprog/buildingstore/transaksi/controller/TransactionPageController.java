package id.ac.ui.cs.advprog.buildingstore.transaksi.controller;

import id.ac.ui.cs.advprog.buildingstore.authentication.model.User;
import id.ac.ui.cs.advprog.buildingstore.authentication.repository.UserRepository;
import id.ac.ui.cs.advprog.buildingstore.transaksi.model.Transaction;
import id.ac.ui.cs.advprog.buildingstore.transaksi.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TransactionPageController {

    private final TransactionService transactionService;
    private final UserRepository userRepository;

    @GetMapping("/transaksi")
    public String cashierTransactions(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<Transaction> transactions;
        if (user.getRole().name().equals("ADMIN")) {
            transactions = transactionService.getAllTransactions();
        } else {
            transactions = transactionService.getTransactionsByUser(user);
        }

        model.addAttribute("transactions", transactions);
        model.addAttribute("username", user.getUsername());

        return "transaksi/listTransaksi";
    }
}
