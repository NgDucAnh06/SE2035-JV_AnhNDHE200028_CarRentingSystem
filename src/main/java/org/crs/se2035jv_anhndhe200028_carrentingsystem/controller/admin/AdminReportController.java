package org.crs.se2035jv_anhndhe200028_carrentingsystem.controller.admin;

import lombok.RequiredArgsConstructor;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.SearchReportDTO;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.service.CarRentalService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/report")
@RequiredArgsConstructor
public class AdminReportController {
    private final CarRentalService carRentalService;

    @GetMapping("/show")
    public String showReport(Model model) {
        model.addAttribute("searchReportDTO", new SearchReportDTO());
        model.addAttribute("report", carRentalService.showReport(new SearchReportDTO()));
        return "view/adminRental/report";
    }

    @PostMapping("/show")
    public String searchReport(@ModelAttribute("searchReportDTO") SearchReportDTO searchReportDTO, Model model) {
        model.addAttribute("report", carRentalService.showReport(searchReportDTO));
        return "view/adminRental/report";
    }
}
