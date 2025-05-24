package id.ac.ui.cs.advprog.buildingstore.authentication.controller;

import id.ac.ui.cs.advprog.buildingstore.authentication.dto.ChangePasswordRequest;
import id.ac.ui.cs.advprog.buildingstore.authentication.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model, Principal principal) {
        model.addAttribute("username", principal.getName());
        return "admin/dashboard";
    }

    @GetMapping("/cashier/dashboard")
    public String cashierDashboard(Model model, Principal principal) {
        model.addAttribute("username", principal.getName());
        return "cashier/dashboard";
    }

    @GetMapping("/change-password")
    public String changePasswordPage(Model model) {
        model.addAttribute("changePasswordRequest", new ChangePasswordRequest());
        return "change_password";
    }

    @PostMapping("/change-password")
    public String changePassword(@Valid @ModelAttribute ChangePasswordRequest request,
                                 BindingResult result,
                                 Principal principal,
                                 Model model) {
        if (result.hasErrors()) {
            model.addAttribute("username", principal.getName());
            return "profile";
        }

        try {
            authService.changePassword(request, principal.getName());
            return "redirect:/profile?success";
        } catch (Exception e) {
            return "redirect:/profile?error";
        }
    }

    @GetMapping("/profile")
    public String profilePage(Model model, Principal principal) {
        model.addAttribute("username", principal.getName());
        model.addAttribute("changePasswordRequest", new ChangePasswordRequest());
        return "profile";
    }
}
