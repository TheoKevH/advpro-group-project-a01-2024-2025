package id.ac.ui.cs.advprog.buildingstore.authentication.security;

import id.ac.ui.cs.advprog.buildingstore.authentication.model.Role;
import id.ac.ui.cs.advprog.buildingstore.authentication.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CustomUserDetailsTest {

    @Test
    void testCustomUserDetailsMethods() {
        User user = new User();
        user.setUsername("kasir");
        user.setPassword("secret");
        user.setRole(Role.CASHIER);

        CustomUserDetails details = new CustomUserDetails(user);

        assertEquals("kasir", details.getUsername());
        assertEquals("secret", details.getPassword());
        assertTrue(details.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_CASHIER")));

        assertTrue(details.isAccountNonExpired());
        assertTrue(details.isAccountNonLocked());
        assertTrue(details.isCredentialsNonExpired());
        assertTrue(details.isEnabled());
    }

    @Test
    void testGetUserReturnsCorrectUser() {
        User user = new User();
        user.setUsername("dicky");
        user.setPassword("rahasia");
        user.setRole(Role.ADMIN);

        CustomUserDetails details = new CustomUserDetails(user);
        assertEquals(user, details.getUser());
    }

}
