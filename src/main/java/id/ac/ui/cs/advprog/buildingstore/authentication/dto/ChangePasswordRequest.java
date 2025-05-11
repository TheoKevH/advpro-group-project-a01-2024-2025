package id.ac.ui.cs.advprog.buildingstore.authentication.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangePasswordRequest {
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;

}

