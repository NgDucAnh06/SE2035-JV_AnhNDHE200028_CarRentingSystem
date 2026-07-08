package org.crs.se2035jv_anhndhe200028_carrentingsystem.repository;

import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByAccountName(String accountName);
    Account findAccountByAccountNameAndPassword(String accountName, String password);
    Account findAccountByEmail(String email);
    List<Account> findByRole(String role);

    Account findAccountByAccountID(Long accountID);
    void deleteAccountByAccountID(Long accountID);
}
