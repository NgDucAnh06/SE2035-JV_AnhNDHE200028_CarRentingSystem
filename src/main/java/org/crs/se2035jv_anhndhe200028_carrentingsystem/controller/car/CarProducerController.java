package org.crs.se2035jv_anhndhe200028_carrentingsystem.controller.car;

import lombok.RequiredArgsConstructor;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.CarProducer;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.exception.DuplicateResourceException;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.service.CarProducerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/producer")
@RequiredArgsConstructor
public class CarProducerController {

    private final CarProducerService carProducerService;

    //create
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("carProducer", new CarProducer());
        return "view/producer/producerCreate";
    }

    @PostMapping("/create")
    public String processCreateProducer(@ModelAttribute("carProducer") CarProducer carProducer, BindingResult bindingResult) {
        try {
            carProducerService.save(carProducer);
            return "redirect:/producer/create?success";
        } catch (DuplicateResourceException ex) {
            bindingResult.rejectValue("producerName", "error.producerName", ex.getMessage());
            return "view/producer/producerCreate";
        }
    }

    //update
    @GetMapping("/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        CarProducer carProducer = carProducerService.findByProducerID(id);
        if (carProducer == null) {
            return "redirect:/producer/list?error=notfound";
        }
        model.addAttribute("carProducer", carProducer);
        return "view/producer/producerCreate";
    }

    @PostMapping("/update")
    public String processUpdateProducer(@ModelAttribute("carProducer") CarProducer carProducer, BindingResult bindingResult) {
        try {
            carProducerService.update(carProducer);
            return "redirect:/producer/list?success";
        } catch (DuplicateResourceException ex) {
            bindingResult.rejectValue("producerName", "error.producerName", ex.getMessage());
            return "view/producer/producerCreate";
        }
    }
}
