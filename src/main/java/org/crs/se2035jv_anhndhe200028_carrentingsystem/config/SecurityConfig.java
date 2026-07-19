package org.crs.se2035jv_anhndhe200028_carrentingsystem.config;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Account;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.repository.AccountRepository;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.service.impl.AccountUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AccountUserDetailsService userDetailsService;
    private final AccountRepository accountRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/auth/signin",
                                "/auth/signup",
                                "/css/**",
                                "/favicon.ico",
                                "/error"
                        ).permitAll()
                        .requestMatchers(
                                "/adminCustomer/**",
                                "/adminRental/**",
                                "/report/**",
                                "/car/**",
                                "/producer/**"
                        ).hasRole("ADMIN")
                        .requestMatchers("/carRental/**", "/review/**").hasRole("CUSTOMER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/auth/signin")
                        .loginProcessingUrl("/auth/signin")
                        .usernameParameter("accountName")
                        .passwordParameter("password")
                        .failureUrl("/auth/signin?error=true")
                        .successHandler((request, response, authentication) -> {
                            Account account = accountRepository.findByAccountName(authentication.getName())
                                    .orElseThrow(() -> new IllegalStateException("Authenticated account not found."));

                            HttpSession session = request.getSession();
                            session.setAttribute("account", account);
                            if (account.getCustomer() != null) {
                                session.setAttribute("customer", account.getCustomer());
                            } else {
                                session.removeAttribute("customer");
                            }

                            String destination = "ADMIN".equalsIgnoreCase(account.getRole())
                                    ? "/adminCustomer/list"
                                    : "/home";
                            response.sendRedirect(request.getContextPath() + destination);
                        })
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/auth/signin?logout=true")
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendRedirect(request.getContextPath() + "/auth/signin"))
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                response.sendRedirect(request.getContextPath() + "/home?accessDenied=true"))
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
