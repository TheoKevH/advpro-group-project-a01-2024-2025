package id.ac.ui.cs.advprog.buildingstore.authentication.service;
import id.ac.ui.cs.advprog.buildingstore.authentication.dto.RegisterRequest;

public interface AuthService {
    void register(RegisterRequest request);
    void changePassword(String username, String oldPassword, String newPassword);
}
