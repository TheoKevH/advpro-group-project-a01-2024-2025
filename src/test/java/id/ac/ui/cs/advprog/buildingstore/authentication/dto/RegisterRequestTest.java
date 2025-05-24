package id.ac.ui.cs.advprog.buildingstore.authentication.dto;

import id.ac.ui.cs.advprog.buildingstore.authentication.model.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegisterRequestTest {

    @Test
    void testGettersAndSetters() {
        RegisterRequest req = new RegisterRequest();
        req.setUsername("kasir1");
        req.setPassword("securePass123");
        req.setRole(Role.CASHIER);

        assertEquals("kasir1", req.getUsername());
        assertEquals("securePass123", req.getPassword());
        assertEquals(Role.CASHIER, req.getRole());
    }
}
