package id.ac.ui.cs.advprog.buildingstore.authentication.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChangePasswordRequestTest {

    @Test
    void testGettersAndSetters() {
        ChangePasswordRequest req = new ChangePasswordRequest();
        req.setOldPassword("old");
        req.setNewPassword("newpass123");
        req.setConfirmPassword("newpass123");

        assertEquals("old", req.getOldPassword());
        assertEquals("newpass123", req.getNewPassword());
        assertEquals("newpass123", req.getConfirmPassword());
    }
}
