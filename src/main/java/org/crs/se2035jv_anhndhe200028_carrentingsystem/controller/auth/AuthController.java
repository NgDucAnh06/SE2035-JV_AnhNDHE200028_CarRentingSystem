package org.crs.se2035jv_anhndhe200028_carrentingsystem.controller.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.LoginRequest;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.RegisterRequest;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.exception.CustomValidationException;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    //signin
    @GetMapping("/signin")
    public String showSignin(@RequestParam(required = false) String error,
                             @RequestParam(required = false) String logout,
                             Model model) {
        model.addAttribute("account", new LoginRequest());
        if (error != null) {
            model.addAttribute("loginError", "Wrong account name or password!");
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "You have been signed out successfully.");
        }
        return "view/auth/signin";
    }

    //signup
    @GetMapping("/signup")
    public String showSignup(Model model) {
        model.addAttribute("registerDTO", new RegisterRequest());
        return "view/auth/signup";
    }

    @PostMapping("/signup")
    public String processSignup(@Valid @ModelAttribute("registerDTO") RegisterRequest registerRequest,
                                BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "view/auth/signup";
        }

        try {
            userService.signup(registerRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Account is created successfully!");
            return "redirect:/auth/signin";
        } catch (CustomValidationException ex) {
            ex.getErrors().forEach((field, message) -> bindingResult.rejectValue(field, "error." + field, message));
            return "view/auth/signup";
        }
    }

}
