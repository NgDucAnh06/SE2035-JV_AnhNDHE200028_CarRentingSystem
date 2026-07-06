package org.crs.se2035jv_anhndhe200028_carrentingsystem.repository;

import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountName(String accountName);
    Account findAccountByAccountNameAndPassword(String accountName, String password);
    Account findAccountByEmail(String email);
}
