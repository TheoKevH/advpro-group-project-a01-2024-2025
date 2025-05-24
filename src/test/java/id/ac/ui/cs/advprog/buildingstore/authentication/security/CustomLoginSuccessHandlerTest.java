package id.ac.ui.cs.advprog.buildingstore.authentication.security;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.TestingAuthenticationToken;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomLoginSuccessHandlerTest {

    private final CustomLoginSuccessHandler handler = new CustomLoginSuccessHandler();

    @Test
    void testRedirectToAdminDashboard() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        Authentication auth = new TestingAuthenticationToken(
                "admin", "password", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

        handler.onAuthenticationSuccess(request, response, auth);

        assertEquals("/admin/dashboard", response.getRedirectedUrl());
    }

    @Test
    void testRedirectToCashierDashboard() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        Authentication auth = new TestingAuthenticationToken(
                "cashier", "password", List.of(new SimpleGrantedAuthority("ROLE_CASHIER")));

        handler.onAuthenticationSuccess(request, response, auth);

        assertEquals("/cashier/dashboard", response.getRedirectedUrl());
    }

    @Test
    void testRedirectToDefaultDashboard() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        Authentication auth = new TestingAuthenticationToken(
                "guest", "password", List.of()); // no roles

        handler.onAuthenticationSuccess(request, response, auth);

        assertEquals("/dashboard", response.getRedirectedUrl());
    }
    @Test
    void testRedirectToDefaultWhenRoleIsUnrecognized() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        Authentication auth = new TestingAuthenticationToken(
                "weirdUser", "password", List.of(new SimpleGrantedAuthority("ROLE_UNKNOWN")));

        CustomLoginSuccessHandler handler = new CustomLoginSuccessHandler();
        handler.onAuthenticationSuccess(request, response, auth);

        assertEquals("/dashboard", response.getRedirectedUrl());
    }

}
