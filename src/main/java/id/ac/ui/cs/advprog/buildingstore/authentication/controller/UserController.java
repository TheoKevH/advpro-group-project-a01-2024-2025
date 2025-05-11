package id.ac.ui.cs.advprog.buildingstore.authentication.controller;

import id.ac.ui.cs.advprog.buildingstore.authentication.dto.RegisterRequest;
import id.ac.ui.cs.advprog.buildingstore.authentication.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    public String registerUser(@ModelAttribute RegisterRequest request) {
        authService.register(request);
        return "redirect:/admin/users";
    }
}
