package pl.com.app.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import pl.com.app.dto.SpecializationDTO;
import pl.com.app.service.SpecializationService;
import pl.com.app.validators.SpecializationValidator;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/specializations")
@RequiredArgsConstructor
public class SpecializationController {

    private final SpecializationService specializationService;
    private final SpecializationValidator specializationValidator;

    @InitBinder
    private void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.setValidator(specializationValidator);
    }

    @GetMapping("/add")
    public String addSpecializations(Model model) {
        model.addAttribute("specialization", new SpecializationDTO());
        model.addAttribute("errors", new HashMap<>());
        model.addAttribute("errorsGlobal", new ArrayList<>());
        return "specializations/add";
    }

    @PostMapping("/add")
    public String addSpecializations(@Valid @ModelAttribute SpecializationDTO specializationDTO,
                                     BindingResult bindingResult,
                                     Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("specialization", specializationDTO);
            model.addAttribute("errors", bindingResult
                    .getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getCode)));
            model.addAttribute("errorsGlobal", bindingResult
                    .getGlobalErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList()));
            return "specializations/add";
        }
        specializationService.addSpecialization(specializationDTO);
        return "redirect:/specializations";
    }

    @GetMapping(value = "/modify/{id}")
    public String editSpecialization(@PathVariable Long id,
                                     Model model) {
        model.addAttribute("specialization", specializationService.getOneSpecialization(id));
        model.addAttribute("errors", new HashMap<>());
        model.addAttribute("errorsGlobal", new ArrayList<>());
        return "specializations/modify";
    }

    @PostMapping(value = "/modify")
    public String editSpecialization(@Valid @ModelAttribute SpecializationDTO specializationDTO,
                                     BindingResult bindingResult,
                                     Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("specialization", specializationDTO);
            model.addAttribute("errors", bindingResult
                    .getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getCode)));
            model.addAttribute("errorsGlobal", bindingResult
                    .getGlobalErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList()));
            return "specializations/modify";
        }
        specializationService.modifySpecialization(specializationDTO);
        return "redirect:/specializations";
    }

    @GetMapping
    public String getAllSpecializations(Model model) {
        model.addAttribute("specializations", specializationService.getAllSpecializations());
        return "specializations/all";
    }

    @GetMapping("/{id}")
    public String getOneSpecialization(@PathVariable Long id,
                                       Model model) {
        model.addAttribute("specialization", specializationService.getOneSpecialization(id));
        return "specializations/one";
    }

    @PostMapping("/delete")
    public String deleteSpecialization(@RequestParam Long id) {
        specializationService.deleteSpecialization(id);
        return "redirect:/specializations";
    }
}
