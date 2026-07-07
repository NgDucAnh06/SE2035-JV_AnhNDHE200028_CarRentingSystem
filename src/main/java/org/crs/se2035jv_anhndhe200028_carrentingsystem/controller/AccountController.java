package org.crs.se2035jv_anhndhe200028_carrentingsystem.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.UpdateProfileRequestDTO;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Account;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Customer;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.repository.AccountRepository;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.repository.CustomerRepository;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final UserService userService;

    @GetMapping("/updateProfile")
    public String updateProfile(Model model, HttpSession session) {
        UpdateProfileRequestDTO updateProfileDTO = new UpdateProfileRequestDTO();
        Account account = (Account) session.getAttribute("account");
        Customer customer = (Customer) session.getAttribute("customer");

        updateProfileDTO.setEmail(account.getEmail());
        updateProfileDTO.setFullName(customer.getFullName());
        updateProfileDTO.setMobile(customer.getMobile());
        updateProfileDTO.setBirthday(customer.getBirthday());
        updateProfileDTO.setIdentityCard(customer.getIdentityCard());
        updateProfileDTO.setLicenceNum(customer.getLicenceNumber());
        updateProfileDTO.setLicenceDate(customer.getLicenceDate());

        model.addAttribute("updateProfileDTO", updateProfileDTO);
        return "account/updateProfile";
    }

    @PostMapping("/updateProfile")
    public String processUpdateProfile(@Valid @ModelAttribute("updateProfileDTO") UpdateProfileRequestDTO updateProfileDTO,
                                       BindingResult bindingResult,
                                       HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "account/updateProfile";
        }

        userService.updateProfile(updateProfileDTO, bindingResult, session);

        if (bindingResult.hasErrors()) {
            return "account/updateProfile";
        }

        return "redirect:/home"; 
    }
}
