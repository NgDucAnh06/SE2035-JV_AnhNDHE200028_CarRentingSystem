package org.crs.se2035jv_anhndhe200028_carrentingsystem.repository;

import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Account findByAccountName(String accountName);
    Account findAccountByAccountNameAndPassword(String accountName, String password);
    Account findAccountByEmail(String email);
    List<Account> findByRole(String role);

    @Query("""
            SELECT a FROM Account a
            JOIN a.customer c
            WHERE LOWER(a.role) = LOWER(:role)
              AND (:keyword = ''
                   OR LOWER(c.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')))
            """)
    Page<Account> searchByCustomerName(@Param("role") String role,
                                       @Param("keyword") String keyword,
                                       Pageable pageable);

    Account findAccountByAccountID(Integer accountID);
}
