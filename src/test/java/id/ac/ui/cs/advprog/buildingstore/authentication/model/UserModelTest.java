package id.ac.ui.cs.advprog.buildingstore.authentication.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserModelTest {

    @Test
    void testGettersAndSetters() {
        User user = new User();
        user.setId(10L);
        user.setUsername("admin");
        user.setPassword("hash");
        user.setRole(Role.ADMIN);

        assertEquals(10L, user.getId());
        assertEquals("admin", user.getUsername());
        assertEquals("hash", user.getPassword());
        assertEquals(Role.ADMIN, user.getRole());
    }
}
