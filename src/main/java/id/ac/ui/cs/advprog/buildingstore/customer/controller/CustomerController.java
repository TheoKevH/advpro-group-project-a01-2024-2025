package id.ac.ui.cs.advprog.buildingstore.customer.controller;

import id.ac.ui.cs.advprog.buildingstore.customer.model.Customer;
import id.ac.ui.cs.advprog.buildingstore.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/edit/{id}")
    public String editCustomerPage(@PathVariable String id, Model model) {
        Customer customer = customerService.getCustomer(id);
        model.addAttribute("customer", customer);
        return "customer/editCustomer";
    }

    @PostMapping("/edit")
    public String editCustomerPost(@ModelAttribute Customer customer, Model model) {
        customerService.addCustomer(customer);
        return "redirect:list";
    }

    @GetMapping("/delete/{id}")
    public String deleteCustomerForm(@PathVariable String id, Model model) {
        customerService.deleteCustomer(id);
        return "redirect:list";
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        try {
            List<Customer> customers = customerService.getAllCustomers();

            if (customers.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
