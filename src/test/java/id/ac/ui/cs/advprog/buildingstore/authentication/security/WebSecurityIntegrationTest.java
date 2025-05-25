package id.ac.ui.cs.advprog.buildingstore.authentication.security;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class WebSecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testLoginPageIsAccessible() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAdminAccessAllowed() throws Exception {
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "cashier", roles = {"CASHIER"})
    void testCashierAccessDeniedToAdminPath() throws Exception {
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "cashier", roles = {"CASHIER"})
    void testCashierAccessAllowed() throws Exception {
        mockMvc.perform(get("/cashier/dashboard"))
                .andExpect(status().isOk());
    }
}
