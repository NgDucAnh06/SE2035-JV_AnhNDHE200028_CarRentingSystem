package org.crs.se2035jv_anhndhe200028_carrentingsystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Account;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.repository.AccountRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AccountUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String accountName) throws UsernameNotFoundException {
        Account account = accountRepository.findByAccountName(accountName)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found."));

        String role = account.getRole().toUpperCase(Locale.ROOT);
        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }

        return User.builder()
                .username(account.getAccountName())
                .password(account.getPassword())
                .authorities(new SimpleGrantedAuthority(role))
                .build();
    }
}
