package org.crs.se2035jv_anhndhe200028_carrentingsystem.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.LoginRequestDTO;
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
        if(bindingResult.hasErrors()) {
            return "auth/signin";
        }


    }
}
