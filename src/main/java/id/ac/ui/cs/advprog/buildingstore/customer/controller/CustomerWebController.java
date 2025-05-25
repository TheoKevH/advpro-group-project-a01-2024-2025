package id.ac.ui.cs.advprog.buildingstore.customer.controller;

import id.ac.ui.cs.advprog.buildingstore.authentication.model.Role;
import id.ac.ui.cs.advprog.buildingstore.authentication.model.User;
import id.ac.ui.cs.advprog.buildingstore.customer.model.Customer;
import id.ac.ui.cs.advprog.buildingstore.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/customers")
public class CustomerWebController {
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

    @GetMapping("/")
    public String listCustomers(Model model) {
        List<Customer> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);
        return "customer/listCustomer";
    }

    @GetMapping("")
    public String customersHomepage(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        User user = (User) authentication.getPrincipal();
        if (user.getRole() == Role.ADMIN) {
            // Admin view - get all customers
            List<Customer> customers = customerService.getAllCustomers();
            model.addAttribute("customers", customers);
            model.addAttribute("isAdmin", true);
        } else {
            // Regular user view
            Customer customer = customerService.getCustomerByUserId(user.getId());
            if (customer != null) {
                model.addAttribute("customer", customer);
                model.addAttribute("hasCustomerAccount", true);
            } else {
                model.addAttribute("hasCustomerAccount", false);
            }
        }

        List<Customer> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);
        return "customer/customerHomepage";
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
}
