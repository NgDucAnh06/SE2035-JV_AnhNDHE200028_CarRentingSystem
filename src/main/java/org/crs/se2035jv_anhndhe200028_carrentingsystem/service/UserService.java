package org.crs.se2035jv_anhndhe200028_carrentingsystem.service;

import jakarta.servlet.http.HttpSession;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.LoginRequestDTO;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.RegisterRequestDTO;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.UpdateProfileRequestDTO;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Account;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface UserService {
    Account signin(LoginRequestDTO loginRequestDTO);
    void signup(RegisterRequestDTO registerRequestDTO);
    void updateProfile(UpdateProfileRequestDTO updateProfileRequestDTO, HttpSession session);
    List<Account> getAllAccount();
    List<Account> getAccountsByRole(String role);
    Account getAccountById(Long id);
    void delete(Long id);
}
