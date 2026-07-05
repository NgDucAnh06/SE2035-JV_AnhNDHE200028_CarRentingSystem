package org.crs.se2035jv_anhndhe200028_carrentingsystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.LoginRequestDTO;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Account;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.repository.AccountRepository;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AccountRepository accountRepository;

    @Override
    public Account signin(LoginRequestDTO loginRequestDTO) {
        Account account = accountRepository.findByAccountName(loginRequestDTO.getAccountName())
                .orElseThrow(() -> new RuntimeException("Account not found!"));
        if (!account.getPassword().equals(loginRequestDTO.getPassword())) {
            throw new RuntimeException("Invalid password! Please try again!");
        }
        return account;
    }
}
