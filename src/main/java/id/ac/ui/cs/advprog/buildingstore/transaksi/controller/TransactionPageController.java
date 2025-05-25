package id.ac.ui.cs.advprog.buildingstore.transaksi.controller;

import id.ac.ui.cs.advprog.buildingstore.authentication.model.User;
import id.ac.ui.cs.advprog.buildingstore.authentication.repository.UserRepository;
import id.ac.ui.cs.advprog.buildingstore.product.model.Product;
import id.ac.ui.cs.advprog.buildingstore.transaksi.dto.CreateTransactionRequest;
import id.ac.ui.cs.advprog.buildingstore.transaksi.dto.ProductDTO;
import id.ac.ui.cs.advprog.buildingstore.transaksi.model.Transaction;
import id.ac.ui.cs.advprog.buildingstore.transaksi.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TransactionPageController {

    private final TransactionService transactionService;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;


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

    @GetMapping("/transaksi/new")
    public String showCreateTransactionPage(Model model) {
        ProductDTO[] products = restTemplate.getForObject(
                "http://localhost:8080/api/product", ProductDTO[].class
        );

        model.addAttribute("products", List.of(products));
        model.addAttribute("createRequest", new CreateTransactionRequest());

        return "transaksi/createNewTransaksi";
    }


}
