package id.ac.ui.cs.advprog.buildingstore.authentication.controller;

import id.ac.ui.cs.advprog.buildingstore.authentication.dto.RegisterRequest;
import id.ac.ui.cs.advprog.buildingstore.authentication.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class UserController {

    private static final String USERS_ATTR = "users";
    private static final String REGISTER_REQUEST_ATTR = "registerRequest";
    private static final String ERROR_MESSAGE_ATTR = "errorMessage";
    private static final String REGISTER_USER_VIEW = "admin/register_user";
    private static final String REDIRECT_USERS = "redirect:/admin/users";

    private final AuthService authService;

    public UserController(AuthService authService) {
        this.authService = authService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public String userList(Model model) {
        model.addAttribute(USERS_ATTR, authService.getAllUsers());
        return "admin/user_list";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/register")
    public String registerUserForm(Model model) {
        model.addAttribute(REGISTER_REQUEST_ATTR, new RegisterRequest());
        return REGISTER_USER_VIEW;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/users/register")
    public String registerUser(@Valid @ModelAttribute RegisterRequest request, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute(REGISTER_REQUEST_ATTR, request);
            return REGISTER_USER_VIEW;
        }
        try {
            authService.register(request);
        } catch (IllegalArgumentException e) {
            model.addAttribute(REGISTER_REQUEST_ATTR, request);
            model.addAttribute(ERROR_MESSAGE_ATTR, e.getMessage());
            return REGISTER_USER_VIEW;
        }

        return REDIRECT_USERS;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        authService.deleteUser(id);
        return REDIRECT_USERS;
    }
}
