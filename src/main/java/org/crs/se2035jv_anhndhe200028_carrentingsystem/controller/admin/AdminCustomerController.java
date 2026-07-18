package org.crs.se2035jv_anhndhe200028_carrentingsystem.controller.admin;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Account;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/adminCustomer")
@RequiredArgsConstructor
public class AdminCustomerController {
    private static final int PAGE_SIZE = 10;

    private final UserService userService;

    //list
    @GetMapping("/list")
    public String customerList(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "") String keyword,
                               Model model) {
        String normalizedKeyword = keyword.trim();
        Page<Account> accountPage = userService.searchCustomersByName(
                normalizedKeyword,
                PageRequest.of(Math.max(page, 0), PAGE_SIZE, Sort.by("accountID").descending())
        );
        model.addAttribute("accountPage", accountPage);
        model.addAttribute("accounts", accountPage.getContent());
        model.addAttribute("keyword", normalizedKeyword);
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
    public String customerDelete(@PathVariable("id") Integer id, HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        Account currentAccount = (Account) session.getAttribute("account");
        if (currentAccount == null) {
            return "redirect:/auth/signin";
        }
        if (!"ADMIN".equalsIgnoreCase(currentAccount.getRole())) {
            redirectAttributes.addFlashAttribute("errorMessage", "You are not authorized to delete customers.");
            return "redirect:/home";
        }

        try {
            userService.deleteCustomer(id);
            redirectAttributes.addFlashAttribute("message", "Customer deleted successfully.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }
        return "redirect:/adminCustomer/list";
    }
}
