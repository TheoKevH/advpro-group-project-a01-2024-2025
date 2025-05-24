package id.ac.ui.cs.advprog.buildingstore.authentication.service;

import id.ac.ui.cs.advprog.buildingstore.authentication.dto.ChangePasswordRequest;
import id.ac.ui.cs.advprog.buildingstore.authentication.dto.RegisterRequest;
import id.ac.ui.cs.advprog.buildingstore.authentication.model.Role;
import id.ac.ui.cs.advprog.buildingstore.authentication.model.User;
import id.ac.ui.cs.advprog.buildingstore.authentication.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterSuccess() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newuser");
        request.setPassword("securepass");
        request.setRole(Role.CASHIER);

        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("securepass")).thenReturn("encodedpass");

        authService.register(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals("newuser", savedUser.getUsername());
        assertEquals("encodedpass", savedUser.getPassword());
        assertEquals(Role.CASHIER, savedUser.getRole());
    }

    @Test
    void testGetAllUsers() {
        List<User> users = List.of(new User(), new User());
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = authService.getAllUsers();

        assertEquals(2, result.size());
        verify(userRepository).findAll();
    }


    @Test
    void testRegisterDuplicateUsername() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("admin");

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(new User()));

        assertThrows(IllegalArgumentException.class, () -> authService.register(request));
    }

    @Test
    void testChangePasswordSuccess() {
        User user = new User();
        user.setPassword("oldEncoded");
        user.setUsername("test");

        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword("old");
        request.setNewPassword("newpass123");
        request.setConfirmPassword("newpass123");

        when(userRepository.findByUsername("test")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("old", "oldEncoded")).thenReturn(true);
        when(passwordEncoder.encode("newpass123")).thenReturn("newEncoded");

        authService.changePassword(request, "test");

        assertEquals("newEncoded", user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void testChangePasswordOldPasswordInvalid() {
        User user = new User();
        user.setPassword("oldEncoded");

        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword("wrong");
        request.setNewPassword("newpass");
        request.setConfirmPassword("newpass");

        when(userRepository.findByUsername("test")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "oldEncoded")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> authService.changePassword(request, "test"));
    }

    @Test
    void testChangePasswordMismatchConfirm() {
        User user = new User();
        user.setPassword("encoded");

        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword("old");
        request.setNewPassword("newpass");
        request.setConfirmPassword("wrongpass");

        when(userRepository.findByUsername("test")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("old", "encoded")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authService.changePassword(request, "test"));
    }

    @Test
    void testChangePasswordUserNotFound() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword("whatever");
        request.setNewPassword("new");
        request.setConfirmPassword("new");

        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> authService.changePassword(request, "ghost"));
    }


    @Test
    void testDeleteUserSuccess() {
        when(userRepository.existsById(1L)).thenReturn(true);

        authService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void testDeleteUserNotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> authService.deleteUser(1L));
    }
}
