package org.crs.se2035jv_anhndhe200028_carrentingsystem.service.impl;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.LoginRequestDTO;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.RegisterRequestDTO;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.UpdateProfileRequestDTO;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Account;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Customer;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.exception.BadCredentialsException;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.exception.CustomValidationException;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.repository.AccountRepository;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.repository.CustomerRepository;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    @Override
    public Account signin(LoginRequestDTO loginRequestDTO) {
        Account account = accountRepository.findAccountByAccountNameAndPassword(loginRequestDTO.getAccountName(), loginRequestDTO.getPassword());
        if (account == null) {
            throw new BadCredentialsException("Wrong account name or password!");
        }
        return account;
    }

    @Override
    public void signup(RegisterRequestDTO registerRequestDTO) {
        java.util.Map<String, String> errors = new java.util.HashMap<>();

        if (accountRepository.findAccountByEmail(registerRequestDTO.getEmail()) != null) {
            errors.put("email", "Email already exists!");
        }

        if (accountRepository.findByAccountName(registerRequestDTO.getAccountName()) != null) {
            errors.put("accountName", "Account name already exists!");
        }

        if (customerRepository.findCustomerByMobile(registerRequestDTO.getMobile()) != null) {
            errors.put("mobile", "Mobile already exists!");
        }

        if (customerRepository.findCustomerByIdentityCard(registerRequestDTO.getIdentityCard()) != null) {
            errors.put("identityCard", "Identity card already exists!");
        }

        if (customerRepository.findCustomerByLicenceNumber(registerRequestDTO.getLicenceNum()) != null) {
            errors.put("licenceNum", "Licence number already exists!");
        }

        if (!errors.isEmpty()) {
            throw new CustomValidationException(errors);
        }

        Account account = new Account();
        account.setAccountName(registerRequestDTO.getAccountName());
        account.setEmail(registerRequestDTO.getEmail());
        account.setRole("customer");
        account.setPassword(registerRequestDTO.getPassword());
        
        Customer customer = new Customer();
        customer.setAccount(account);
        customer.setBirthday(registerRequestDTO.getBirthday());
        customer.setMobile(registerRequestDTO.getMobile());
        customer.setFullName(registerRequestDTO.getFullName());
        customer.setIdentityCard(registerRequestDTO.getIdentityCard());
        customer.setLicenceNumber(registerRequestDTO.getLicenceNum());
        customer.setLicenceDate(registerRequestDTO.getLicenceDate());

        account.setCustomer(customer);
        accountRepository.save(account);
    }

    @Override
    public void updateProfile(UpdateProfileRequestDTO updateProfileRequestDTO, HttpSession session) {
        Account sessionAccount = (Account) session.getAttribute("account");
        Integer accountId = sessionAccount.getAccountID();
        Integer customerId = sessionAccount.getCustomer().getCustomerID();

        java.util.Map<String, String> errors = new java.util.HashMap<>();

        Account checkAccount = accountRepository.findAccountByEmail(updateProfileRequestDTO.getEmail());
        if (checkAccount != null && !checkAccount.getAccountID().equals(accountId)) {
            errors.put("email", "Email already exists!");
        }

        Customer checkCustomer = customerRepository.findCustomerByMobile(updateProfileRequestDTO.getMobile());
        if (checkCustomer != null && !checkCustomer.getCustomerID().equals(customerId)) {
            errors.put("mobile", "Mobile already exists!");
        }

        checkCustomer = customerRepository.findCustomerByIdentityCard(updateProfileRequestDTO.getIdentityCard());
        if (checkCustomer != null && !checkCustomer.getCustomerID().equals(customerId)) {
            errors.put("identityCard", "Identity card already exists!");
        }

        checkCustomer = customerRepository.findCustomerByLicenceNumber(updateProfileRequestDTO.getLicenceNum());
        if (checkCustomer != null && !checkCustomer.getCustomerID().equals(customerId)) {
            errors.put("licenceNum", "Licence number already exists!");
        }

        if (!errors.isEmpty()) {
            throw new CustomValidationException(errors);
        }

        Account account = accountRepository.findById(accountId).orElse(sessionAccount);

        account.setEmail(updateProfileRequestDTO.getEmail());

        if (account.getCustomer() != null) {
            account.getCustomer().setBirthday(updateProfileRequestDTO.getBirthday());
            account.getCustomer().setMobile(updateProfileRequestDTO.getMobile());
            account.getCustomer().setFullName(updateProfileRequestDTO.getFullName());
            account.getCustomer().setIdentityCard(updateProfileRequestDTO.getIdentityCard());
            account.getCustomer().setLicenceNumber(updateProfileRequestDTO.getLicenceNum());
            account.getCustomer().setLicenceDate(updateProfileRequestDTO.getLicenceDate());
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

}
