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
@RequestMapping("/admin/customers")
public class CustomerAdminController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private AuthService authService;

    @GetMapping("")
    public String customersHomepage(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = authService.getUserByUsername(username);
        model.addAttribute("user", user);

        List<Customer> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);
        return "admin/customer/homepage";
    }

    @GetMapping("/create")
    public String createCustomerPage(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = authService.getUserByUsername(username);
        model.addAttribute("user", user);

        Customer customer = new Customer();
        model.addAttribute("customer", customer);

        List<User> userList = authService.getAllUsers();
        model.addAttribute("users", userList);
        return "admin/customer/create_customer";
    }

    @GetMapping("/view/{id}")
    public String viewCustomerPage(@PathVariable Long id, Model model) {
        Customer customer = customerService.getCustomer(id);
        model.addAttribute("customer", customer);
        return "admin/customer/view_customer";
    }

    @GetMapping("/edit/{id}")
    public String editCustomerPage(@PathVariable Long id, Model model) {
        Customer customer = customerService.getCustomer(id);
        model.addAttribute("customer", customer);
        return "admin/customer/edit_customer";
    }

    // === POST MAPPING ===
    @PostMapping("/create")
    public String createCustomerPost(@ModelAttribute Customer customer, Model model) {
        Long id = customer.getUser().getId();
        User user = authService.getUserById(id);

        customer.setUser(user);
        customerService.addCustomer(customer);
        return "redirect:/customers";
    }

    @PostMapping("/edit/{id}")
    public String editCustomerPost(@PathVariable Long id, @ModelAttribute Customer customer, Model model) {
        Long userId = customer.getUser().getId();
        User user = authService.getUserById(userId);

        customer.setUser(user);
        customerService.updateCustomer(customer);
        return "redirect:/customers";
    }

    @PostMapping("/delete/{id}")
    public String deleteCustomerPost(@PathVariable Long id, Model model) {
        customerService.deleteCustomer(id);
        return "redirect:/customers";
    }
}
