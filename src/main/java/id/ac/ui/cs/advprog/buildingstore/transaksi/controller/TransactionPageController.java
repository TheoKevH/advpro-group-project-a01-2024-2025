package id.ac.ui.cs.advprog.buildingstore.transaksi.controller;

import id.ac.ui.cs.advprog.buildingstore.authentication.model.User;
import id.ac.ui.cs.advprog.buildingstore.authentication.repository.UserRepository;
import id.ac.ui.cs.advprog.buildingstore.product.model.Product;
import id.ac.ui.cs.advprog.buildingstore.product.service.ProductService;
import id.ac.ui.cs.advprog.buildingstore.transaksi.dto.CreateTransactionRequest;
import id.ac.ui.cs.advprog.buildingstore.transaksi.dto.CustomerDTO;
import id.ac.ui.cs.advprog.buildingstore.transaksi.dto.ProductDTO;
import id.ac.ui.cs.advprog.buildingstore.transaksi.enums.TransactionStatus;
import id.ac.ui.cs.advprog.buildingstore.transaksi.model.Transaction;
import id.ac.ui.cs.advprog.buildingstore.transaksi.model.TransactionItem;
import id.ac.ui.cs.advprog.buildingstore.transaksi.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class TransactionPageController {

    private final TransactionService transactionService;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);


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

        transactions = transactions.stream()
                .filter(trx -> trx.getStatus() != TransactionStatus.CANCELLED)
                .toList();


        String customerUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/customers")
                .toUriString();
        CustomerDTO[] customers = restTemplate.getForObject(customerUrl, CustomerDTO[].class);

        Map<String, String> customerMap = new HashMap<>();
        if (customers != null) {
            for (CustomerDTO customer : customers) {
                customerMap.put(customer.getId(), customer.getName());
            }
        }

        model.addAttribute("transactions", transactions);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("customerMap", customerMap);

        return "transaksi/listTransaksi";
    }

    @GetMapping("/transaksi/new")
    public String showCreateTransactionPage(Model model) {
        String customerUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/customers")
                .toUriString();

        String productUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/product")
                .toUriString();

        ProductDTO[] products = restTemplate.getForObject(productUrl, ProductDTO[].class);

        CustomerDTO[] customers = restTemplate.getForObject(customerUrl, CustomerDTO[].class);

        model.addAttribute("products", products != null ? List.of(products) : List.of());
        model.addAttribute("customers", customers != null ? List.of(customers) : List.of());
        model.addAttribute("createRequest", new CreateTransactionRequest());

        return "transaksi/createNewTransaksi";
    }

    @PostMapping("/transaksi/create")
    public String createTransaction(@ModelAttribute CreateTransactionRequest request) {
        System.out.println(">>> Customer ID: " + request.getCustomerId());
        System.out.println(">>> Items: " + request.getItems());
        transactionService.createTransaction(request.getCustomerId(), request.getItems());

        for (TransactionItem item : request.getItems()) {
            String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/product/" + item.getProductId())
                    .toUriString();
            restTemplate.put(url, item.getQuantity());
        }

        return "redirect:/transaksi";
    }


}
