package org.crs.se2035jv_anhndhe200028_carrentingsystem.service;

import jakarta.servlet.http.HttpSession;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.ChangePassRequest;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.LoginRequest;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.RegisterRequest;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.UpdateProfileRequest;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Account;

import java.util.List;

public interface UserService {
    Account signin(LoginRequest loginRequest);
    void signup(RegisterRequest registerRequest);
    void updateProfile(UpdateProfileRequest updateProfileRequest, HttpSession session);
    List<Account> getAllAccount();
    List<Account> getAccountsByRole(String role);
    Account getAccountById(Integer id);
    void delete(Integer id);
    void changePassword(Account account, ChangePassRequest changePassRequest, HttpSession session);
}
