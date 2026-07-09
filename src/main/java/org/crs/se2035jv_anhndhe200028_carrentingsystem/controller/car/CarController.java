package org.crs.se2035jv_anhndhe200028_carrentingsystem.controller.car;

import lombok.RequiredArgsConstructor;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Car;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.exception.DuplicateResourceException;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.repository.CarProducerRepository;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.service.CarService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/car")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;
    private final CarProducerRepository carProducerRepository;

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("car", new Car());
        model.addAttribute("producers", carProducerRepository.findAll());
        return "view/car/carCreate";
    }

    @PostMapping("/create")
    public String processCreateCar(@ModelAttribute("car") Car car, BindingResult bindingResult, Model model) {
        try {
            carService.save(car);
            return "redirect:/car/create?success";
        } catch (DuplicateResourceException ex) {
            bindingResult.rejectValue("carName", "error.carName", ex.getMessage());
            model.addAttribute("producers", carProducerRepository.findAll());
            return "view/car/carCreate";
        }
    }
}
