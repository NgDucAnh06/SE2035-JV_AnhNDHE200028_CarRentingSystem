package org.crs.se2035jv_anhndhe200028_carrentingsystem.controller.auth;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.LoginRequest;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.RegisterRequest;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Account;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.exception.BadCredentialsException;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.exception.CustomValidationException;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    //signin
    @GetMapping("/signin")
    public String showSignin(Model model) {
        model.addAttribute("account", new LoginRequest());
        return "view/auth/signin";
    }

    @PostMapping("/signin")
    public String processSignin(@Valid @ModelAttribute("account") LoginRequest loginRequest,
                                BindingResult bindingResult,
                                HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "view/auth/signin";
        }

        try {
            Account account = userService.signin(loginRequest);
            if (account.getCustomer() != null) {
                session.setAttribute("customer", account.getCustomer());
            }
            session.setAttribute("account", account);
            return "redirect:/home";
        } catch (BadCredentialsException ex) {
            bindingResult.reject("loginFailed", ex.getMessage());
            return "view/auth/signin";
        }
    }

    //signup
    @GetMapping("/signup")
    public String showSignup(Model model) {
        model.addAttribute("registerDTO", new RegisterRequest());
        return "view/auth/signup";
    }

    @PostMapping("/signup")
    public String processSignup(@Valid @ModelAttribute("registerDTO") RegisterRequest registerRequest,
                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "view/auth/signup";
        }

        try {
            userService.signup(registerRequest);
            return "redirect:/auth/signin";
        } catch (CustomValidationException ex) {
            ex.getErrors().forEach((field, message) -> bindingResult.rejectValue(field, "error." + field, message));
            return "view/auth/signup";
        }
    }
}
