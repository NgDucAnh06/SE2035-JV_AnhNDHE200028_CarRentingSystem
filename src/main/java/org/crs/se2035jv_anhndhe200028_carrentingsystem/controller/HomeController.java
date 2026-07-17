package org.crs.se2035jv_anhndhe200028_carrentingsystem.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Account;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.service.CarService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final CarService carService;

    @GetMapping({"/", "/home"})
    public String home(HttpSession session, Model model) {
        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            return "redirect:/auth/signin";
        }
        
        if ("ADMIN".equalsIgnoreCase(account.getRole())) {
            return "redirect:/adminCustomer/list";
        } else {
            model.addAttribute("cars", carService.findAvailableCars());
            return "view/home/home";
        }
    }
}
