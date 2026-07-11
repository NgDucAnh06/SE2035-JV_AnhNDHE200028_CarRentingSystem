package org.crs.se2035jv_anhndhe200028_carrentingsystem.controller.admin;

import lombok.RequiredArgsConstructor;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.service.CarRentalService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.RentalSummaryDTO;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequestMapping("/adminRental")
@RequiredArgsConstructor
public class AdminRentalController {

    private final CarRentalService carRentalService;

    @GetMapping("/rentalManagement")
    public String showRentalManagement(Model model) {
        List<RentalSummaryDTO> rentals = carRentalService.showManagement();
        model.addAttribute("rentals", rentals);
        return "view/adminRental/list";
    }

    @PostMapping("/{id}/complete")
    public String completeRental(@PathVariable("id") Integer id) {
        carRentalService.updateStatus(id, org.crs.se2035jv_anhndhe200028_carrentingsystem.enums.RentalStatus.COMPLETED.name());
        return "redirect:/adminRental/rentalManagement";
    }

    @PostMapping("/{id}/cancel")
    public String cancelRental(@PathVariable("id") Integer id) {
        carRentalService.updateStatus(id, org.crs.se2035jv_anhndhe200028_carrentingsystem.enums.RentalStatus.CANCELLED.name());
        return "redirect:/adminRental/rentalManagement";
    }
}
