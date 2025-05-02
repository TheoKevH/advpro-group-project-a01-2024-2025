package id.ac.ui.cs.advprog.buildingstore.customer.controller;

import id.ac.ui.cs.advprog.buildingstore.customer.model.Customer;
import id.ac.ui.cs.advprog.buildingstore.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping("/register")
    public String registerCustomerPage(Model model) {
        Customer customer = new Customer();
        model.addAttribute("customer", customer);
        return "customer/registerCustomer";
    }

    @PostMapping("/register")
    public String registerCustomerPost(@ModelAttribute Customer customer, Model model) {
        customerService.addCustomer(customer);
        return "redirect:list";
    }

    @GetMapping("/list")
    public String listCustomers(Model model) {
        List<Customer> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);
        return "customer/listCustomers";
    }
}
