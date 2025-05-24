package id.ac.ui.cs.advprog.buildingstore.authentication.controller;

import id.ac.ui.cs.advprog.buildingstore.authentication.dto.RegisterRequest;
import id.ac.ui.cs.advprog.buildingstore.authentication.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private final AuthService authService;

    public UserController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/admin/users")
    public String userList(Model model) {
        model.addAttribute("users", authService.getAllUsers());
        return "admin/user_list";
    }

    @GetMapping("/admin/users/register")
    public String registerUserForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "admin/register_user";
    }

    @PostMapping("/admin/users/register")
    public String registerUser(@Valid @ModelAttribute RegisterRequest request, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("registerRequest", request);
            return "admin/register_user";
        }
        try {
            authService.register(request);
        } catch (IllegalArgumentException e) {
            model.addAttribute("registerRequest", request);
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/register_user";
        }

        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        authService.deleteUser(id);
        return "redirect:/admin/users";
    }
}
