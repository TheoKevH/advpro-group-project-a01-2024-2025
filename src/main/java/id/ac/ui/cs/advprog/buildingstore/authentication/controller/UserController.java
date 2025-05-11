package id.ac.ui.cs.advprog.buildingstore.authentication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/admin/users")
    public String userListPage() {
        return "admin/user_list";
    }
}
