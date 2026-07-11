package org.crs.se2035jv_anhndhe200028_carrentingsystem.controller.car;

import lombok.RequiredArgsConstructor;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Car;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.exception.DuplicateResourceException;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.repository.CarProducerRepository;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.service.CarProducerService;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.service.CarService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/car")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;
    private final CarProducerService carProducerService;

    //create
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("car", new Car());
        model.addAttribute("producers", carProducerService.getAll());
        return "view/car/carCreate";
    }

    @PostMapping("/create")
    public String processCreateCar(@ModelAttribute("car") Car car, BindingResult bindingResult, Model model) {
        try {
            carService.save(car);
            return "redirect:/car/create?success";
        } catch (DuplicateResourceException ex) {
            bindingResult.rejectValue("carName", "error.carName", ex.getMessage());
            model.addAttribute("producers", carProducerService.getAll());
            return "view/car/carCreate";
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
        return "view/car/carCreate";
    }

    @PostMapping("/update")
    public String processUpdateCar(@ModelAttribute("car") Car car, BindingResult bindingResult, Model model) {
        try {
            carService.update(car);
            return "redirect:/car/list?success";
        } catch (DuplicateResourceException ex) {
            bindingResult.rejectValue("carName", "error.carName", ex.getMessage());
            model.addAttribute("producers", carProducerService.getAll());
            return "view/car/carCreate";
        }
    }
}
