package id.ac.ui.cs.advprog.buildingstore.customer.controller;

import id.ac.ui.cs.advprog.buildingstore.authentication.model.User;
import id.ac.ui.cs.advprog.buildingstore.authentication.service.AuthService;
import id.ac.ui.cs.advprog.buildingstore.customer.model.Customer;
import id.ac.ui.cs.advprog.buildingstore.customer.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/customers")
public class CustomerControllerPage {
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
        return "customer/homepage";
    }

    @GetMapping("/create")
    public String createCustomerPage(Authentication authentication, Model model) {
        Customer customer = new Customer();
        model.addAttribute("customer", customer);
        return "customer/create_customer";
    }

    @GetMapping("/view/{id}")
    public String viewCustomerPage(@PathVariable Long id, Model model) {
        Customer customer = customerService.getCustomer(id);
        model.addAttribute("customer", customer);
        return "customer/view_customer";
    }

    @GetMapping("/edit/{id}")
    public String editCustomerPage(@PathVariable Long id, Model model) {
        Customer customer = customerService.getCustomer(id);
        model.addAttribute("customer", customer);
        return "customer/edit_customer";
    }

    // === POST MAPPING ===
    @PostMapping("/create")
    public String createCustomerPost(@Valid @ModelAttribute Customer customer, BindingResult bindingResult, Model model) {
        if (customerService.existsByEmail(customer.getEmail())) {
            bindingResult.rejectValue("email", "duplicate.email", "Email is already in use");
        }

        if (customerService.existsByPhone(customer.getPhone())) {
            bindingResult.rejectValue("phone", "duplicate.phone", "Phone is already in use");
        }

        if (bindingResult.hasErrors()) {
            return "customer/create_customer";
        }

        try {
            customerService.addCustomer(customer);
            return "redirect:/customers";
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("email")) {
                bindingResult.rejectValue("email", "duplicate.email", "Email is already in use");
            } else if (e.getMessage().contains("phone")) {
                bindingResult.rejectValue("phone", "duplicate.phone", "Phone is already in use");
            } else {
                model.addAttribute("errorMessage", "Failed to create customer due to data constraint");
            }

            return "customer/create_customer";
        } catch (Exception e) {
            // Handle any other business logic errors
            model.addAttribute("errorMessage", "Failed to create customer");
            return "customer/create_customer";
        }
    }

    @PostMapping("/edit")
    public String editCustomerPost(@Valid @ModelAttribute Customer customer, BindingResult bindingResult, Model model) {
        if (customerService.existsByEmailAndIdNot(customer.getEmail(), customer.getId())) {
            bindingResult.rejectValue("email", "duplicate.email", "Email is already in use");
        }
        if (customerService.existsByPhoneAndIdNot(customer.getPhone(), customer.getId())) {
            bindingResult.rejectValue("phone", "duplicate.phone", "Phone is already in use");
        }
        if (bindingResult.hasErrors()) {
            return "customer/edit_customer";
        }

        try {
            customerService.updateCustomer(customer);
            return "redirect:/customers";
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("email")) {
                bindingResult.rejectValue("email", "duplicate.email", "Email is already in use");
            } else if (e.getMessage().contains("phone")) {
                bindingResult.rejectValue("phone", "duplicate.phone", "Phone is already in use");
            } else {
                model.addAttribute("errorMessage", "Failed to create customer due to data constraint");
            }

            return "customer/edit_customer";
        } catch (Exception e) {
            // Handle any other business logic errors
            model.addAttribute("errorMessage", "Failed to create customer");
            return "customer/edit_customer";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteCustomerPost(@PathVariable Long id, Model model) {
        customerService.deleteCustomer(id);
        return "redirect:/customers";
    }
}
