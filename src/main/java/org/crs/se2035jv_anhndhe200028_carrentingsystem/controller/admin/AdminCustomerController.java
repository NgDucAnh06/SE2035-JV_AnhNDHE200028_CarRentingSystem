package org.crs.se2035jv_anhndhe200028_carrentingsystem.controller.admin;

import lombok.RequiredArgsConstructor;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Account;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/adminCustomer")
@RequiredArgsConstructor
public class AdminCustomerController {
    private final UserService userService;

    //list
    @GetMapping("/list")
    public String customerList(Model model) {
        List<Account> accountList = userService.getAccountsByRole("customer");
        model.addAttribute("accounts", accountList);
        return "view/adminCustomer/list";
    }

    //detail
    @GetMapping("/{id}")
    public String customerDetail(@PathVariable("id") Integer id, Model model) {
        Account account = userService.getAccountById(id);
        model.addAttribute("account", account);
        return "view/adminCustomer/detail";
    }

    //delete
    @PostMapping("/{id}/delete")
    public String customerDelete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        userService.delete(id);
        redirectAttributes.addFlashAttribute("message", "Deleted successfully the account with id: " + id);
        return "redirect:/adminCustomer/list";
    }
}
