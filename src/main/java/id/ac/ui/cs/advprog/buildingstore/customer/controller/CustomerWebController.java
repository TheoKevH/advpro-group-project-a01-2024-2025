package id.ac.ui.cs.advprog.buildingstore.customer.controller;

import id.ac.ui.cs.advprog.buildingstore.authentication.model.Role;
import id.ac.ui.cs.advprog.buildingstore.authentication.model.User;
import id.ac.ui.cs.advprog.buildingstore.authentication.service.AuthService;
import id.ac.ui.cs.advprog.buildingstore.customer.model.Customer;
import id.ac.ui.cs.advprog.buildingstore.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private AuthService authService;

    @GetMapping("/create")
    public String createCustomerPage(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = authService.getUser(username);
        Customer customer = new Customer(user);

        model.addAttribute("customer", customer);
        return "customer/createCustomer";
    }

    @PostMapping("/create")
    public String createCustomerPost(@ModelAttribute Customer customer, Model model) {
        String username = customer.getUser().getUsername();
        User user = authService.getUser(username);
        customer.setUser(user);

        customerService.addCustomer(customer);
        return "redirect:/customers";
    }

    @GetMapping("")
    public String customersHomepage(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String username = authentication.getName();
        User user = authService.getUser(username);
        model.addAttribute("user", user);

        if (user.getRole() == Role.ADMIN) {
            // Admin view - get all customers
            List<Customer> customers = customerService.getAllCustomers();
            model.addAttribute("customers", customers);
            model.addAttribute("isAdmin", true);
            model.addAttribute("hasCustomerAccount", false);
        } else {
            // Regular user view
            Customer customer = customerService.getCustomerByUser(user);
            model.addAttribute("customer", customer);
            model.addAttribute("isAdmin", false);
            model.addAttribute("hasCustomerAccount", customer != null);
        }

        List<Customer> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);
        return "customer/customerHomepage";
    }

    @GetMapping("/edit")
    public String editCustomerPage(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = authService.getUser(username);
        Customer customer = customerService.getCustomerByUser(user);

        model.addAttribute("customer", customer);
        return "customer/editCustomer";
    }

    @PostMapping("/edit")
    public String editCustomerPost(@ModelAttribute Customer customer, Authentication authentication, Model model) {
        String username = customer.getUser().getUsername();
        User user = authService.getUser(username);
        customer.setUser(user);

        customerService.updateCustomer(customer);
        return "redirect:/customers";
    }

    @PostMapping("/delete")
    public String deleteCustomerForm(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = authService.getUser(username);
        Customer customer = customerService.getCustomerByUser(user);
        customerService.deleteCustomer(customer.getId());
        return "redirect:/customers";
    }
}
