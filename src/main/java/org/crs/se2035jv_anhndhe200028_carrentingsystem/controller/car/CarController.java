package org.crs.se2035jv_anhndhe200028_carrentingsystem.controller.car;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Account;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Car;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.exception.DuplicateResourceException;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.service.CarProducerService;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.service.CarService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/car")
@RequiredArgsConstructor
public class CarController {
    private static final int PAGE_SIZE = 10;

    private final CarService carService;
    private final CarProducerService carProducerService;

    @GetMapping("/list")
    public String carList(@RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "") String keyword,
                          Model model) {
        String normalizedKeyword = keyword.trim();
        Page<Car> carPage = carService.searchCarsByName(
                normalizedKeyword,
                PageRequest.of(Math.max(page, 0), PAGE_SIZE, Sort.by("carID").descending())
        );
        model.addAttribute("carPage", carPage);
        model.addAttribute("cars", carPage.getContent());
        model.addAttribute("keyword", normalizedKeyword);
        return "view/car/list";
    }

    //create
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("car", new Car());
        model.addAttribute("producers", carProducerService.getAll());
        return "view/car/create";
    }

    @PostMapping("/create")
    public String processCreateCar(@ModelAttribute("car") Car car, BindingResult bindingResult, Model model) {
        try {
            carService.save(car);
            return "redirect:/car/create?success";
        } catch (DuplicateResourceException ex) {
            bindingResult.rejectValue("carName", "error.carName", ex.getMessage());
            model.addAttribute("producers", carProducerService.getAll());
            return "view/car/create";
        }
    }

    //update
    @GetMapping("/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Car car = carService.findById(id);
        if (car == null) {
            return "redirect:/car/list?error=notfound";
        }
        model.addAttribute("car", car);
        model.addAttribute("producers", carProducerService.getAll());
        return "view/car/create";
    }

    @PostMapping("/update")
    public String processUpdateCar(@ModelAttribute("car") Car car, BindingResult bindingResult, Model model) {
        try {
            carService.update(car);
            return "redirect:/car/list?success";
        } catch (DuplicateResourceException ex) {
            bindingResult.rejectValue("carName", "error.carName", ex.getMessage());
            model.addAttribute("producers", carProducerService.getAll());
            return "view/car/create";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteCar(@PathVariable Integer id, HttpSession session,
                            RedirectAttributes redirectAttributes) {
        Account currentAccount = (Account) session.getAttribute("account");
        if (currentAccount == null) {
            return "redirect:/auth/signin";
        }
        if (!"ADMIN".equalsIgnoreCase(currentAccount.getRole())) {
            redirectAttributes.addFlashAttribute("errorMessage", "You are not authorized to delete cars.");
            return "redirect:/home";
        }

        try {
            carService.deleteCar(id);
            redirectAttributes.addFlashAttribute("successMessage", "Car deleted successfully.");
        } catch (IllegalArgumentException | IllegalStateException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", "Unable to delete the car because car is still in renting.");
        }
        return "redirect:/car/list";
    }
}
