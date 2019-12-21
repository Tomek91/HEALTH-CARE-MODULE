package pl.com.app.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import pl.com.app.dto.WorkplaceDTO;
import pl.com.app.service.AddressService;
import pl.com.app.service.WorkplaceService;
import pl.com.app.validators.WorkspaceValidator;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/workplaces")
@RequiredArgsConstructor
public class WorkplaceController {

    private final WorkplaceService workplaceService;
    private final AddressService addressService;
    private final WorkspaceValidator workspaceValidator;

    @InitBinder
    private void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.setValidator(workspaceValidator);
    }

    @GetMapping("/add")
    public String addWorkplaces(Model model) {
        model.addAttribute("workplace", new WorkplaceDTO());
        model.addAttribute("addresses", addressService.getAllAddresses());
        model.addAttribute("errors", new HashMap<>());
        model.addAttribute("errorsGlobal", new ArrayList<>());
        return "workplaces/add";
    }

    @PostMapping("/add")
    public String addWorkplaces(@Valid @ModelAttribute WorkplaceDTO workplaceDTO,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("workplace", workplaceDTO);
            model.addAttribute("addresses", addressService.getAllAddresses());
            model.addAttribute("errors", bindingResult
                    .getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getCode)));
            model.addAttribute("errorsGlobal", bindingResult
                    .getGlobalErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList()));
            return "workplaces/add";
        }
        workplaceService.addWorkplace(workplaceDTO);
        return "redirect:/workplaces";
    }

    @GetMapping(value = "/modify/{id}")
    public String editWorkplace(@PathVariable Long id,
                                Model model) {
        model.addAttribute("workplace", workplaceService.getOneWorkplace(id));
        model.addAttribute("addresses", addressService.getAllAddresses());
        model.addAttribute("errors", new HashMap<>());
        model.addAttribute("errorsGlobal", new ArrayList<>());
        return "workplaces/modify";
    }

    @PostMapping(value = "/modify")
    public String editWorkplace(@Valid @ModelAttribute WorkplaceDTO workplaceDTO,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("workplace", workplaceDTO);
            model.addAttribute("addresses", addressService.getAllAddresses());
            model.addAttribute("errors", bindingResult
                    .getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getCode)));
            model.addAttribute("errorsGlobal", bindingResult
                    .getGlobalErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList()));
            return "workplaces/modify";
        }
        workplaceService.modifyWorkplace(workplaceDTO);
        return "redirect:/workplaces";
    }

    @GetMapping
    public String getAllWorkplaces(Model model) {
        model.addAttribute("workplaces", workplaceService.getAllWorkplaces());
        return "workplaces/all";
    }

    @GetMapping("/{id}")
    public String getOneWorkplace(@PathVariable Long id,
                                  Model model) {
        model.addAttribute("workplace", workplaceService.getOneWorkplace(id));
        return "workplaces/one";
    }

    @PostMapping("/delete")
    public String deleteWorkplace(@RequestParam Long id) {
        workplaceService.deleteWorkplace(id);
        return "redirect:/workplaces";
    }
}
