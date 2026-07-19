package org.crs.se2035jv_anhndhe200028_carrentingsystem.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.ReviewDTO;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.CarRental;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Customer;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.enums.RentalStatus;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.repository.CarRentalRepository;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.service.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final CarRentalRepository carRentalRepository;

    @GetMapping("/{carRenId}/form")
    public String showReviewForm(@PathVariable Integer carRenId, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/auth/signin";
        }

        CarRental carRental = carRentalRepository.findById(carRenId).orElse(null);
        if (carRental == null || !carRental.getCustomer().getCustomerID().equals(customer.getCustomerID())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Rental not found or access denied.");
            return "redirect:/carRental/history";
        }

        if (carRental.getStatus() != RentalStatus.COMPLETED) {
            redirectAttributes.addFlashAttribute("errorMessage", "Can only review completed rentals.");
            return "redirect:/carRental/detail/" + carRenId;
        }

        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setCarRenID(carRenId);

        model.addAttribute("carRental", carRental);
        model.addAttribute("reviewDTO", reviewDTO);
        return "view/review/form";
    }

    @PostMapping("/submit")
    public String submitReview(@Valid @ModelAttribute("reviewDTO") ReviewDTO reviewDTO, BindingResult bindingResult,
                               HttpSession session, RedirectAttributes redirectAttributes, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/auth/signin";
        }

        if (bindingResult.hasErrors()) {
            CarRental carRental = carRentalRepository.findById(reviewDTO.getCarRenID()).orElse(null);
            model.addAttribute("carRental", carRental);
            return "view/review/form";
        }

        try {
            reviewService.submitReview(reviewDTO.getCarRenID(), reviewDTO.getReviewStar(), reviewDTO.getComment());
            redirectAttributes.addFlashAttribute("successMessage", "Thank you for your review!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/carRental/detail/" + reviewDTO.getCarRenID();
    }
}
