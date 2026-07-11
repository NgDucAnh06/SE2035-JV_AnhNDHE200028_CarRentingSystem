package org.crs.se2035jv_anhndhe200028_carrentingsystem.controller.carRental;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Car;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.CarRental;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Customer;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.service.CarRentalService;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.service.CarService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/carRental")
@RequiredArgsConstructor
public class CarRentalController {

    private final CarService carService;
    private final CarRentalService carRentalService;

    //register
    @GetMapping("/rentalRegister")
    public String showRentalRegisterForm(Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/auth/signin";
        }

        List<Car> availableCars = carService.findAvailableCars();
        model.addAttribute("cars", availableCars);
        model.addAttribute("carRental", new CarRental());
        return "view/carRental/register";
    }

    @PostMapping("/rentalRegister")
    public String processRentalRegister(
            @RequestParam(value = "carIds", required = false) List<Integer> carIds,
            @RequestParam("pickupDate") LocalDate pickupDate,
            @RequestParam("returnDate") LocalDate returnDate,
            HttpSession session) {

        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/auth/signin";
        }

        if (carIds == null || carIds.isEmpty()) {
            return "redirect:/carRental/rentalRegister?error=NoCarsSelected";
        }

        carRentalService.createRentals(customer, carIds, pickupDate, returnDate);

        return "redirect:/home";
    }

    //history
    @GetMapping("/history")
    public String viewHistory(HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/auth/signin";
        }
        List<CarRental> carRentalList = carRentalService.getAllCarRentalByCustomer(customer);
        model.addAttribute("rentalList", carRentalList);
        return "view/carRental/history";
    }
}
