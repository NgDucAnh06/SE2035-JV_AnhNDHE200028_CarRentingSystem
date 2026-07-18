package org.crs.se2035jv_anhndhe200028_carrentingsystem.controller.car;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Account;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.CarProducer;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.exception.DuplicateResourceException;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.service.CarProducerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/producer")
@RequiredArgsConstructor
public class CarProducerController {
    private static final int PAGE_SIZE = 10;

    private final CarProducerService carProducerService;

    @GetMapping("/list")
    public String producerList(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "") String keyword,
                               Model model) {
        String normalizedKeyword = keyword.trim();
        Page<CarProducer> producerPage = carProducerService.searchProducersByName(
                normalizedKeyword,
                PageRequest.of(Math.max(page, 0), PAGE_SIZE, Sort.by("producerID").descending())
        );
        model.addAttribute("producerPage", producerPage);
        model.addAttribute("producers", producerPage.getContent());
        model.addAttribute("keyword", normalizedKeyword);
        return "view/producer/list";
    }

    //create
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("carProducer", new CarProducer());
        return "view/producer/create";
    }

    @PostMapping("/create")
    public String processCreateProducer(@ModelAttribute("carProducer") CarProducer carProducer, BindingResult bindingResult) {
        try {
            carProducerService.save(carProducer);
            return "redirect:/producer/create?success";
        } catch (DuplicateResourceException ex) {
            bindingResult.rejectValue("producerName", "error.producerName", ex.getMessage());
            return "view/producer/create";
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
        return "view/producer/create";
    }

    @PostMapping("/update")
    public String processUpdateProducer(@ModelAttribute("carProducer") CarProducer carProducer, BindingResult bindingResult) {
        try {
            carProducerService.update(carProducer);
            return "redirect:/producer/list?success";
        } catch (DuplicateResourceException ex) {
            bindingResult.rejectValue("producerName", "error.producerName", ex.getMessage());
            return "view/producer/create";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteProducer(@PathVariable Integer id, HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        Account currentAccount = (Account) session.getAttribute("account");
        if (currentAccount == null) {
            return "redirect:/auth/signin";
        }
        if (!"ADMIN".equalsIgnoreCase(currentAccount.getRole())) {
            redirectAttributes.addFlashAttribute("errorMessage", "You are not authorized to delete producers.");
            return "redirect:/home";
        }

        try {
            carProducerService.deleteProducer(id);
            redirectAttributes.addFlashAttribute("successMessage", "Producer deleted successfully.");
        } catch (IllegalArgumentException | IllegalStateException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", "Unable to delete the producer because car is still in renting.");
        }
        return "redirect:/producer/list";
    }
}
