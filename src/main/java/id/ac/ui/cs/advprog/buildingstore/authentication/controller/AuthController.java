package id.ac.ui.cs.advprog.buildingstore.authentication.controller;

import id.ac.ui.cs.advprog.buildingstore.authentication.dto.ChangePasswordRequest;
import id.ac.ui.cs.advprog.buildingstore.authentication.dto.RegisterRequest;
import id.ac.ui.cs.advprog.buildingstore.authentication.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute RegisterRequest request) {
        authService.register(request);
        return "redirect:/login";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/cashier/dashboard")
    public String cashierDashboard() {
        return "cashier/dashboard";
    }

    @GetMapping("/change-password")
    public String changePasswordPage(Model model) {
        model.addAttribute("changePasswordRequest", new ChangePasswordRequest());
        return "change_password";
    }

    @PostMapping("/change-password")
    public String changePassword(@ModelAttribute ChangePasswordRequest request, Principal principal) {
        try {
            authService.changePassword(request, principal.getName());
            return "redirect:/profile?success";
        } catch (Exception e) {
            return "redirect:/profile?error";
        }
    }

    @GetMapping("/profile")
    public String profilePage(Model model) {
        model.addAttribute("changePasswordRequest", new ChangePasswordRequest());
        return "profile";
    }



}
