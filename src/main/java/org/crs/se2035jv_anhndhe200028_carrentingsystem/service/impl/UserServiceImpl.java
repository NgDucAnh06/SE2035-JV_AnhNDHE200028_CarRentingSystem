package org.crs.se2035jv_anhndhe200028_carrentingsystem.service.impl;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.LoginRequestDTO;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.RegisterRequestDTO;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.UpdateProfileRequestDTO;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Account;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Customer;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.repository.AccountRepository;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.repository.CustomerRepository;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    @Override
    public Account signin(LoginRequestDTO loginRequestDTO) {
        Account account = accountRepository.findAccountByAccountNameAndPassword(loginRequestDTO.getAccountName(), loginRequestDTO.getPassword());
        return account;
    }

    @Override
    public void signup(RegisterRequestDTO registerRequestDTO, BindingResult bindingResult) {
        if (accountRepository.findAccountByEmail(registerRequestDTO.getEmail()) != null) {
            bindingResult.rejectValue("email", "error.email", "Email already exists!");
        }

        if (accountRepository.findByAccountName(registerRequestDTO.getAccountName()) != null) {
            bindingResult.rejectValue("accountName", "error.accountName", "Account name already exists!");
        }

        if (customerRepository.findCustomerByMobile(registerRequestDTO.getMobile()) != null) {
            bindingResult.rejectValue("mobile", "error.mobile", "Mobile already exists!");
        }

        if (customerRepository.findCustomerByIdentityCard(registerRequestDTO.getIdentityCard()) != null) {
            bindingResult.rejectValue("identityCard", "error.identityCard", "Identity card already exists!");
        }

        if (customerRepository.findCustomerByLicenceNumber(registerRequestDTO.getLicenceNum()) != null) {
            bindingResult.rejectValue("licenceNum", "error.licenceNum", "Licence number already exists!");
        }

        if (bindingResult.hasErrors()) {
            return;
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
    public void updateProfile(UpdateProfileRequestDTO updateProfileRequestDTO, BindingResult bindingResult, HttpSession session) {
        Account sessionAccount = (Account) session.getAttribute("account");
        long accountId = sessionAccount.getAccountID();
        long customerId = sessionAccount.getCustomer().getCustomerID();

        Account checkAccount = accountRepository.findAccountByEmail(updateProfileRequestDTO.getEmail());
        if (checkAccount != null && checkAccount.getAccountID() != accountId) {
            bindingResult.rejectValue("email", "error.email", "Email already exists!");
        }

        Customer checkCustomer = customerRepository.findCustomerByMobile(updateProfileRequestDTO.getMobile());
        if (checkCustomer != null && checkCustomer.getCustomerID() != customerId) {
            bindingResult.rejectValue("mobile", "error.mobile", "Mobile already exists!");
        }

        checkCustomer = customerRepository.findCustomerByIdentityCard(updateProfileRequestDTO.getIdentityCard());
        if (checkCustomer != null && checkCustomer.getCustomerID() != customerId) {
            bindingResult.rejectValue("identityCard", "error.identityCard", "Identity card already exists!");
        }

        checkCustomer = customerRepository.findCustomerByLicenceNumber(updateProfileRequestDTO.getLicenceNum());
        if (checkCustomer != null && checkCustomer.getCustomerID() != customerId) {
            bindingResult.rejectValue("licenceNum", "error.licenceNum", "Licence number already exists!");
        }

        if (bindingResult.hasErrors()) {
            return;
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
}
