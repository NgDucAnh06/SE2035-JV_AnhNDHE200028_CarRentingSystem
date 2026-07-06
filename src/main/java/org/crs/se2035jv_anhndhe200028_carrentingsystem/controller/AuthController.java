package org.crs.se2035jv_anhndhe200028_carrentingsystem.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.LoginRequestDTO;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.RegisterRequestDTO;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Account;
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

    @GetMapping("/signin")
    public String showSignin(Model model) {
        model.addAttribute("account", new LoginRequestDTO());
        return "auth/signin";
    }

    @PostMapping("/signin")
    public String processSignin(@Valid @ModelAttribute("account") LoginRequestDTO loginRequestDTO,
                                BindingResult bindingResult,
                                HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "auth/signin";
        }

        Account account = userService.signin(loginRequestDTO);
        if (account == null) {
            bindingResult.reject("loginFailed", "Wrong account name or password!");
            return "auth/signin";
        }
        session.setAttribute("account", account);
        return "redirect:/home";
    }


    @GetMapping("/signup")
    public String showSignup(Model model) {
        model.addAttribute("registerDTO", new RegisterRequestDTO());
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String processSignup(@Valid @ModelAttribute("registerDTO") RegisterRequestDTO registerRequestDTO,
                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "auth/signup";
        }

        userService.signup(registerRequestDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            return "auth/signup";
        }

        return "redirect:/auth/signin";
    }
}
