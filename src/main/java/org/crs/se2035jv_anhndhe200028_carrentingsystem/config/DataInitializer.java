package org.crs.se2035jv_anhndhe200028_carrentingsystem.config;

import lombok.RequiredArgsConstructor;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Account;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Customer;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.repository.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AccountRepository accountRepository;

    @Override
    public void run(String... args) throws Exception {
        // Initialize admin account
        if (accountRepository.findByAccountName("admin") == null) {
            Account admin = Account.builder()
                    .accountName("admin")
                    .password("admin")
                    .email("admin@fucar.com")
                    .role("ADMIN")
                    .build();
            accountRepository.save(admin);
        }

        // Initialize test user account
        if (accountRepository.findByAccountName("test") == null) {
            Account testUser = Account.builder()
                    .accountName("test")
                    .password("test@123")
                    .email("test@test.com")
                    .role("customer")
                    .build();

            Customer customer = Customer.builder()
                    .account(testUser)
                    .fullName("Test User")
                    .mobile("0123456789")
                    .identityCard("012345678912")
                    .licenceNumber("L-123456")
                    .licenceDate(LocalDate.now().minusYears(2))
                    .birthday(LocalDate.now().minusYears(20))
                    .build();

            testUser.setCustomer(customer);
            accountRepository.save(testUser);
        }
    }
}
