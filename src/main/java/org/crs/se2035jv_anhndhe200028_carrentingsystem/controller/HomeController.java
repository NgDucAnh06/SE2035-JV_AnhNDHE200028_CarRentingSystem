package org.crs.se2035jv_anhndhe200028_carrentingsystem.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Account;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.service.CarService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final CarService carService;

    @GetMapping({"/", "/home"})
    public String home(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "") String keyword,
                       HttpSession session, Model model) {
        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            return "redirect:/auth/signin";
        }
        
        if ("ADMIN".equalsIgnoreCase(account.getRole())) {
            return "redirect:/adminCustomer/list";
        } else {
            int safePage = Math.max(page, 0);
            PageRequest pageable = PageRequest.of(safePage, 5, Sort.by("carID").descending());
            model.addAttribute("carPage", carService.searchAvailableCars(keyword, pageable));
            model.addAttribute("keyword", keyword.trim());
            return "view/home/home";
        }
    }
}
