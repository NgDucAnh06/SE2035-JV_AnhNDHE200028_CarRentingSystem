package org.crs.se2035jv_anhndhe200028_carrentingsystem.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.RentalReportStatsDTO;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.RentalSummaryDTO;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.SearchReportDTO;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.service.CarRentalService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
@RequestMapping("/report")
@RequiredArgsConstructor
public class AdminReportController {

    private final CarRentalService carRentalService;

    @GetMapping("/show")
    public String showReport(@RequestParam(defaultValue = "0") int page,
                             @Valid @ModelAttribute("searchReportDTO") SearchReportDTO searchReportDTO,
                             BindingResult bindingResult,
                             Model model) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), 10);
        if (bindingResult.hasErrors()) {
            model.addAttribute("reportPage", Page.empty(pageable));
            model.addAttribute("reportStats", new RentalReportStatsDTO(0, BigDecimal.ZERO, 0, 0, 0));
            return "view/adminRental/report";
        }
        Page<RentalSummaryDTO> reportPage = carRentalService.showReport(searchReportDTO, pageable);
        model.addAttribute("reportPage", reportPage);
        model.addAttribute("reportStats", carRentalService.getReportStats(searchReportDTO));
        return "view/adminRental/report";
    }
}
