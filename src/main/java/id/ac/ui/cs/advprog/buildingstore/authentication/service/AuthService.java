package id.ac.ui.cs.advprog.buildingstore.authentication.service;
import id.ac.ui.cs.advprog.buildingstore.authentication.dto.ChangePasswordRequest;
import id.ac.ui.cs.advprog.buildingstore.authentication.dto.RegisterRequest;
import id.ac.ui.cs.advprog.buildingstore.authentication.model.User;
import java.util.List;

public interface AuthService {
    void register(RegisterRequest request);
    void changePassword(ChangePasswordRequest request, String username);
    void deleteUser(Long id);
    List<User> getAllUsers();

}
