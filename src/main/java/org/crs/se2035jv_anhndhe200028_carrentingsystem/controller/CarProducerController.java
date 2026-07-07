package org.crs.se2035jv_anhndhe200028_carrentingsystem.controller;

import lombok.RequiredArgsConstructor;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.CarProducer;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.exception.DuplicateResourceException;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.service.CarProducerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/producer")
@RequiredArgsConstructor
public class CarProducerController {

    private final CarProducerService carProducerService;

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
}
