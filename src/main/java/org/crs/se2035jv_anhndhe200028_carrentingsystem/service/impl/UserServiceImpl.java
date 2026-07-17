package org.crs.se2035jv_anhndhe200028_carrentingsystem.service.impl;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.ChangePassRequest;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.LoginRequest;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.RegisterRequest;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.UpdateProfileRequest;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Account;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Customer;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.exception.BadCredentialsException;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.exception.CustomValidationException;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.repository.AccountRepository;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.repository.CustomerRepository;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.service.UserService;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.utility.InputStandization;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    @Override
    public Account signin(LoginRequest loginRequest) {
        Account account = accountRepository.findAccountByAccountNameAndPassword(loginRequest.getAccountName(), loginRequest.getPassword());
        if (account == null) {
            throw new BadCredentialsException("Wrong account name or password!");
        }
        return account;
    }

    @Override
    public void signup(RegisterRequest registerRequest) {
        java.util.Map<String, String> errors = new java.util.HashMap<>();

        if (accountRepository.findAccountByEmail(registerRequest.getEmail()) != null) {
            errors.put("email", "Email already exists!");
        }

        if (accountRepository.findByAccountName(registerRequest.getAccountName()) != null) {
            errors.put("accountName", "Account name already exists!");
        }

        if (customerRepository.findCustomerByMobile(registerRequest.getMobile()) != null) {
            errors.put("mobile", "Mobile already exists!");
        }

        if (customerRepository.findCustomerByIdentityCard(registerRequest.getIdentityCard()) != null) {
            errors.put("identityCard", "Identity card already exists!");
        }

        if (customerRepository.findCustomerByLicenceNumber(registerRequest.getLicenceNum()) != null) {
            errors.put("licenceNum", "Licence number already exists!");
        }

        if (!errors.isEmpty()) {
            throw new CustomValidationException(errors);
        }

        Account account = Account.builder()
                .accountName(registerRequest.getAccountName())
                .email(registerRequest.getEmail())
                .role("customer")
                .password(registerRequest.getPassword())
                .build();
        
        Customer customer = Customer.builder()
                .account(account)
                .birthday(registerRequest.getBirthday())
                .mobile(registerRequest.getMobile())
                .fullName(registerRequest.getFullName())
                .identityCard(registerRequest.getIdentityCard())
                .licenceNumber(registerRequest.getLicenceNum())
                .licenceDate(registerRequest.getLicenceDate())
                .build();

        account.setCustomer(customer);
        accountRepository.save(account);
    }

    @Override
    public void updateProfile(UpdateProfileRequest updateProfileRequest, HttpSession session) {
        Account sessionAccount = (Account) session.getAttribute("account");
        Integer accountId = sessionAccount.getAccountID();
        Integer customerId = sessionAccount.getCustomer().getCustomerID();

        Map<String, String> errors = new HashMap<>();

        Account checkAccount = accountRepository.findAccountByEmail(updateProfileRequest.getEmail());
        if (checkAccount != null && !checkAccount.getAccountID().equals(accountId)) {
            errors.put("email", "Email already exists!");
        }

        Customer checkCustomer = customerRepository.findCustomerByMobile(updateProfileRequest.getMobile());
        if (checkCustomer != null && !checkCustomer.getCustomerID().equals(customerId)) {
            errors.put("mobile", "Mobile already exists!");
        }

        checkCustomer = customerRepository.findCustomerByIdentityCard(updateProfileRequest.getIdentityCard());
        if (checkCustomer != null && !checkCustomer.getCustomerID().equals(customerId)) {
            errors.put("identityCard", "Identity card already exists!");
        }

        checkCustomer = customerRepository.findCustomerByLicenceNumber(updateProfileRequest.getLicenceNum());
        if (checkCustomer != null && !checkCustomer.getCustomerID().equals(customerId)) {
            errors.put("licenceNum", "Licence number already exists!");
        }

        if (!errors.isEmpty()) {
            throw new CustomValidationException(errors);
        }

        Account account = accountRepository.findById(accountId).orElse(sessionAccount);
        account = account.toBuilder()
                .email(updateProfileRequest.getEmail())
                .build();

        if (account.getCustomer() != null) {
            Customer updatedCustomer = account.getCustomer().toBuilder()
                    .birthday(updateProfileRequest.getBirthday())
                    .mobile(updateProfileRequest.getMobile())
                    .fullName(InputStandization.formatName(updateProfileRequest.getFullName()))
                    .identityCard(updateProfileRequest.getIdentityCard())
                    .licenceNumber(updateProfileRequest.getLicenceNum())
                    .licenceDate(updateProfileRequest.getLicenceDate())
                    .build();
            account = account.toBuilder()
                    .customer(updatedCustomer)
                    .build();
        }

        accountRepository.save(account);

        session.setAttribute("account", account);
        session.setAttribute("customer", account.getCustomer());
    }

    @Override
    public List<Account> getAllAccount() {
        return accountRepository.findAll();
    }

    @Override
    public List<Account> getAccountsByRole(String role) {
        return accountRepository.findByRole(role);
    }

    @Override
    public Account getAccountById(Integer id) {
        return accountRepository.findAccountByAccountID(id);
    }

    @Override
    public void delete(Integer id) {
        accountRepository.deleteAccountByAccountID(id);
    }

    @Override
    public void changePassword(Account account, ChangePassRequest changePassRequest, HttpSession session) {
        Map<String, String> errors = new HashMap<>();

        if (!changePassRequest.getOldPassword().equals(account.getPassword())) {
            errors.put("oldPassword", "Old password is incorrect!");
        }

        if (!changePassRequest.getNewPassword().equals(changePassRequest.getConfirmPassword())) {
            errors.put("confirmPassword", "Confirm password does not match!");
        }

        if (changePassRequest.getNewPassword().equals(account.getPassword())) {
            errors.put("newPassword", "New password must be different from current password!");
        }

        if (!errors.isEmpty()) {
            throw new CustomValidationException(errors);
        }

        account = account.toBuilder()
                .password(changePassRequest.getNewPassword())
                .build();

        accountRepository.save(account);
        session.setAttribute("account", account);
    }

}
