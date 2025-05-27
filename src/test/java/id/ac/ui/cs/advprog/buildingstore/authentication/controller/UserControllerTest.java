package id.ac.ui.cs.advprog.buildingstore.authentication.controller;


import id.ac.ui.cs.advprog.buildingstore.authentication.dto.RegisterRequest;
import id.ac.ui.cs.advprog.buildingstore.authentication.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void admin_ShouldAccessUserListPage() throws Exception {
        when(authService.getAllUsers()).thenReturn(List.of());

        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/user_list"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("User List")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void registerForm_ShouldReturnRegisterView() throws Exception {
        mockMvc.perform(get("/admin/users/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/register_user"))
                .andExpect(content().string(containsString("Register New User")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void registerUser_ShouldRedirectToUserList() throws Exception {
        mockMvc.perform(post("/admin/users/register")
                        .param("username", "newcashier")
                        .param("password", "secret123")
                        .param("role", "CASHIER")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void registerUser_WithValidationErrors_ShouldReturnRegisterView() throws Exception {
        mockMvc.perform(post("/admin/users/register")
                        .param("username", "")
                        .param("password", "short")
                        .param("role", "CASHIER")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/register_user"))
                .andExpect(content().string(containsString("Register New User")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void registerUser_WithDuplicateUsername_ShouldReturnRegisterViewWithError() throws Exception {
        doThrow(new IllegalArgumentException("Username already exists"))
                .when(authService).register(any(RegisterRequest.class));

        mockMvc.perform(post("/admin/users/register")
                        .param("username", "existinguser")
                        .param("password", "validPassword")
                        .param("role", "ADMIN")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/register_user"))
                .andExpect(content().string(containsString("Username already exists")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deleteUser_ShouldRedirectToUserList() throws Exception {
        mockMvc.perform(post("/admin/users/delete/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"));
    }


}