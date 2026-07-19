package org.crs.se2035jv_anhndhe200028_carrentingsystem.service.impl;

import jakarta.servlet.http.HttpSession;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.ChangePassRequest;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.RegisterRequest;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Account;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.repository.AccountRepository;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.repository.CarRentalRepository;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.repository.CarRepository;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.repository.CustomerRepository;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceImplPasswordTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CarRentalRepository carRentalRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private CarRepository carRepository;
    @Mock
    private HttpSession session;

    private PasswordEncoder passwordEncoder;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(
                accountRepository,
                customerRepository,
                carRentalRepository,
                reviewRepository,
                carRepository,
                passwordEncoder
        );
    }

    @Test
    void signupStoresBcryptPasswordInsteadOfPlainText() {
        RegisterRequest request = new RegisterRequest();
        request.setAccountName("customer01");
        request.setPassword("Password1");
        request.setEmail("customer01@example.com");
        request.setFullName("Customer One");
        request.setMobile("0123456789");
        request.setBirthday(LocalDate.of(2000, 1, 1));
        request.setIdentityCard("012345678901");
        request.setLicenceNum("098765432109");
        request.setLicenceDate(LocalDate.now().plusYears(1));

        userService.signup(request);

        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountCaptor.capture());
        String storedPassword = accountCaptor.getValue().getPassword();

        assertThat(storedPassword).isNotEqualTo(request.getPassword());
        assertThat(storedPassword).startsWith("$2");
        assertThat(passwordEncoder.matches(request.getPassword(), storedPassword)).isTrue();
    }

    @Test
    void changePasswordMatchesOldHashAndStoresNewBcryptHash() {
        Account account = Account.builder()
                .accountID(1)
                .accountName("customer01")
                .email("customer01@example.com")
                .role("customer")
                .password(passwordEncoder.encode("OldPassword1"))
                .build();

        ChangePassRequest request = new ChangePassRequest();
        request.setOldPassword("OldPassword1");
        request.setNewPassword("NewPassword2");
        request.setConfirmPassword("NewPassword2");

        userService.changePassword(account, request, session);

        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountCaptor.capture());
        String storedPassword = accountCaptor.getValue().getPassword();

        assertThat(storedPassword).isNotEqualTo(request.getNewPassword());
        assertThat(passwordEncoder.matches(request.getNewPassword(), storedPassword)).isTrue();
        assertThat(passwordEncoder.matches(request.getOldPassword(), storedPassword)).isFalse();
        verify(session).setAttribute("account", accountCaptor.getValue());
    }
}
