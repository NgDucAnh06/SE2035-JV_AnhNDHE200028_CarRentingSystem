package org.crs.se2035jv_anhndhe200028_carrentingsystem.controller;

import jakarta.servlet.http.HttpSession;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Account;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"/", "/home"})
    public String home(HttpSession session) {
        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            return "redirect:/auth/signin";
        }
        
        if ("ADMIN".equalsIgnoreCase(account.getRole())) {
            return "redirect:/adminCustomer/list";
        } else {
            return "view/home/home";
        }
    }
}
