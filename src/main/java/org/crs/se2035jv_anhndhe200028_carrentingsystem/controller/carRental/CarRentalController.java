package org.crs.se2035jv_anhndhe200028_carrentingsystem.controller.carRental;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.SearchHistoryRequest;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.SearchReportDTO;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Car;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.CarRental;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Customer;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.service.CarRentalService;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.service.CarService;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.service.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/carRental")
@RequiredArgsConstructor
public class CarRentalController {

    private final CarService carService;
    private final CarRentalService carRentalService;
    private final ReviewService reviewService;

    //register
    @GetMapping("/rentalRegister")
    public String showRentalRegisterForm(@RequestParam(required = false) Integer selectedCarId,
                                         Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/auth/signin";
        }

        List<Car> availableCars = carService.findAvailableCars();
        model.addAttribute("cars", availableCars);
        model.addAttribute("selectedCarId", selectedCarId);
        model.addAttribute("carRental", new CarRental());
        return "view/carRental/register";
    }

    @PostMapping("/rentalRegister")
    public String processRentalRegister(
            @RequestParam(value = "carIds", required = false) List<Integer> carIds,
            @RequestParam("pickupDate") LocalDate pickupDate,
            @RequestParam("returnDate") LocalDate returnDate,
            HttpSession session, RedirectAttributes redirectAttributes) {

        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/auth/signin";
        }

        if (carIds == null || carIds.isEmpty()) {
            return "redirect:/carRental/rentalRegister?error=NoCarsSelected";
        }

        carRentalService.createRentals(customer, carIds, pickupDate, returnDate);
        redirectAttributes.addFlashAttribute("successMessage", "Rented car successfully!");
        return "redirect:/carRental/history";
    }

    //history
    @GetMapping("/history")
    public String viewHistory(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @ModelAttribute("searchHistoryDTO") SearchHistoryRequest searchHistoryRequest,
                              HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/auth/signin";
        }

        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 20);

        Pageable pageable = PageRequest.of(
                safePage,
                safeSize,
                Sort.by("carRenID").descending()
        );

        Page<CarRental> rentalPage = carRentalService.showHistoryByCustomer(customer, pageable, searchHistoryRequest);
        model.addAttribute("searchHistoryDTO", searchHistoryRequest);
        model.addAttribute("rentalPage", rentalPage);
        model.addAttribute("reviewedRentalIds", reviewService.getReviewedRentalIds(rentalPage.getContent()));
        return "view/carRental/history";
    }

    @PostMapping("/{id}/cancel")
    public String cancelRental(@PathVariable Integer id, HttpSession session,
                               RedirectAttributes redirectAttributes) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/auth/signin";
        }

        try {
            carRentalService.cancelRentalByCustomer(id, customer);
            redirectAttributes.addFlashAttribute("successMessage", "Rental canceled successfully.");
        } catch (IllegalArgumentException | IllegalStateException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }

        return "redirect:/carRental/history";
    }

    @GetMapping("/detail/{id}")
    public String viewDetail(@PathVariable Integer id, HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/auth/signin";
        }

        CarRental carRental = carRentalService.findById(id);
        if (carRental == null || !carRental.getCustomer().getCustomerID().equals(customer.getCustomerID())) {
            return "redirect:/carRental/history";
        }

        org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Review review = reviewService.getReview(carRental);
        model.addAttribute("carRental", carRental);
        model.addAttribute("review", review);
        
        return "view/carRental/detail";
    }
}
