package org.crs.se2035jv_anhndhe200028_carrentingsystem.service;

import jakarta.servlet.http.HttpSession;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.ChangePassRequest;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.RegisterRequest;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.UpdateProfileRequest;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    void signup(RegisterRequest registerRequest);
    void updateProfile(UpdateProfileRequest updateProfileRequest, HttpSession session);
    Page<Account> searchCustomersByName(String keyword, Pageable pageable);
    Account getAccountById(Integer id);
    void deleteCustomer(Integer accountId);
    void changePassword(Account account, ChangePassRequest changePassRequest, HttpSession session);
}
