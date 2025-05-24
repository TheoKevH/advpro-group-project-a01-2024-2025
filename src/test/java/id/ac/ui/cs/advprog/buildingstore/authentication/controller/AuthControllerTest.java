package id.ac.ui.cs.advprog.buildingstore.authentication.controller;

import id.ac.ui.cs.advprog.buildingstore.authentication.dto.ChangePasswordRequest;
import id.ac.ui.cs.advprog.buildingstore.authentication.repository.UserRepository;
import id.ac.ui.cs.advprog.buildingstore.authentication.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private AuthService authService;
    @MockBean private UserRepository userRepository;
    @MockBean private PasswordEncoder passwordEncoder;

    @Test
    public void loginPage_ShouldReturnOkAndContainLoginText() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("username")));
    }

    @Test
    public void login_WithInvalidCredentials_ShouldRedirectToLoginWithError() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "salahuser")
                        .param("password", "salahpass")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void admin_ShouldAccessAdminDashboard() throws Exception {
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Admin Dashboard")));
    }

    @Test
    @WithMockUser(username = "kasir", roles = {"CASHIER"})
    public void cashier_ShouldAccessCashierDashboard() throws Exception {
        mockMvc.perform(get("/cashier/dashboard"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Cashier Dashboard")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void changePasswordPage_ShouldReturnOkAndContainForm() throws Exception {
        mockMvc.perform(get("/change-password"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Change Password")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void changePassword_WithValidInputs_ShouldRedirectWithSuccess() throws Exception {
        mockMvc.perform(post("/change-password")
                        .param("oldPassword", "oldpass")
                        .param("newPassword", "newpass")
                        .param("confirmPassword", "newpass")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile?success"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void changePassword_WithMismatchedConfirm_ShouldRedirectWithError() throws Exception {
        doThrow(new IllegalArgumentException("Password confirmation does not match"))
                .when(authService)
                .changePassword(any(ChangePasswordRequest.class), eq("admin"));

        mockMvc.perform(post("/change-password")
                        .param("oldPassword", "oldpass")
                        .param("newPassword", "newpass")
                        .param("confirmPassword", "wrongconfirm")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile?error"));
    }



}
