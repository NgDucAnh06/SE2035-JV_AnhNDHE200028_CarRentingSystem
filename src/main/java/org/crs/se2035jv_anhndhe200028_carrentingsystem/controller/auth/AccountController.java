package org.crs.se2035jv_anhndhe200028_carrentingsystem.controller.auth;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.ChangePassRequest;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.UpdateProfileRequest;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Account;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Customer;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.exception.CustomValidationException;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final UserService userService;

    @GetMapping("/updateProfile")
    public String updateProfile(Model model, HttpSession session) {
        UpdateProfileRequest updateProfileDTO = new UpdateProfileRequest();
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
        return "view/account/update";
    }

    @PostMapping("/updateProfile")
    public String processUpdateProfile(@Valid @ModelAttribute("updateProfileDTO") UpdateProfileRequest updateProfileDTO,
                                       BindingResult bindingResult,
                                       HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "view/account/update";
        }

        try {
            userService.updateProfile(updateProfileDTO, session);
            return "redirect:/account/updateProfile";
        } catch (CustomValidationException ex) {
            ex.getErrors().forEach((field, message) -> bindingResult.rejectValue(field, "error." + field, message));
            return "view/account/update";
        } 
    }

    @GetMapping("/changePassword")
    public String showChangePassword(Model model) {
        model.addAttribute("changePasswordDTO", new ChangePassRequest());
        return "view/account/changePassword";
    }

    @PostMapping("/changePassword")
    public String processChangePassword(@Valid @ModelAttribute("changePasswordDTO") ChangePassRequest changePassRequest,
                                        BindingResult bindingResult, HttpSession session,
                                        RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "view/account/changePassword";
        }
        Account account = (Account) session.getAttribute("account");

        try {
            userService.changePassword(account, changePassRequest, session);
            redirectAttributes.addFlashAttribute("successMessage", "Password is updated successfully!");
            return "redirect:/account/changePassword";
        } catch (CustomValidationException ex) {
            ex.getErrors().forEach((field, message) -> bindingResult.rejectValue(field, "error." + field, message));
            return "view/account/changePassword";
        }
    }
}
