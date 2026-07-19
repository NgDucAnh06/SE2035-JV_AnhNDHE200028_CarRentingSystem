package org.crs.se2035jv_anhndhe200028_carrentingsystem.controller.admin;

import lombok.RequiredArgsConstructor;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.SearchHistoryRequest;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.CarRental;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.service.CarRentalService;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.service.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.RentalSummaryDTO;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.crs.se2035jv_anhndhe200028_carrentingsystem.enums.RentalStatus.CANCELED;
import static org.crs.se2035jv_anhndhe200028_carrentingsystem.enums.RentalStatus.COMPLETED;
import static org.crs.se2035jv_anhndhe200028_carrentingsystem.enums.RentalStatus.RENTING;

@Controller
@RequestMapping("/adminRental")
@RequiredArgsConstructor
public class AdminRentalController {

    private final CarRentalService carRentalService;
    private final ReviewService reviewService;

    @GetMapping("/rentalManagement")
    public String showRentalManagement(@RequestParam(defaultValue = "0") int page,
                                       @ModelAttribute("searchRentalDTO") SearchHistoryRequest searchRequest,
                                       Model model) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), 10);
        Page<RentalSummaryDTO> rentalPage = carRentalService.showManagement(searchRequest, pageable);
        model.addAttribute("searchRentalDTO", searchRequest);
        model.addAttribute("rentalPage", rentalPage);
        model.addAttribute("rentals", rentalPage.getContent());
        return "view/adminRental/list";
    }

    @GetMapping("/{id}/detail")
    public String showRentalDetail(@PathVariable("id") Integer id, Model model) {
        CarRental carRental = carRentalService.findById(id);
        if (carRental == null) {
            return "redirect:/adminRental/rentalManagement";
        }

        model.addAttribute("carRental", carRental);
        model.addAttribute("review", reviewService.getReview(carRental));
        return "view/adminRental/detail";
    }

    @PostMapping("/{id}/deliver")
    public String deliverRental(@PathVariable("id") Integer id) {
        carRentalService.updateStatus(id, RENTING);
        return "redirect:/adminRental/" + id + "/detail";
    }

    @PostMapping("/{id}/complete")
    public String completeRental(@PathVariable("id") Integer id) {
        carRentalService.updateStatus(id, COMPLETED);
        return "redirect:/adminRental/" + id + "/detail";
    }

    @PostMapping("/{id}/cancel")
    public String cancelRental(@PathVariable("id") Integer id) {
        carRentalService.updateStatus(id, CANCELED);
        return "redirect:/adminRental/" + id + "/detail";
    }
}
