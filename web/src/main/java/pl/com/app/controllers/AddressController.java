package pl.com.app.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import pl.com.app.dto.AddressDTO;
import pl.com.app.service.AddressService;
import pl.com.app.validators.AddressValidator;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;
    private final AddressValidator addressValidator;

    @InitBinder
    private void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.setValidator(addressValidator);
    }


    @GetMapping("/add")
    public String addAddress(Model model) {
        model.addAttribute("address", new AddressDTO());
        model.addAttribute("errors", new HashMap<>());
        model.addAttribute("errorsGlobal", new ArrayList<>());
        return "addresses/add";
    }

    @PostMapping("/add")
    public String addAddress(@Valid @ModelAttribute AddressDTO addressDTO,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("address", addressDTO);
            model.addAttribute("errors", bindingResult
                    .getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getCode)));
            model.addAttribute("errorsGlobal", bindingResult
                    .getGlobalErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList()));
            return "addresses/add";
        }
        addressService.addAddress(addressDTO);
        return "redirect:/addresses";
    }

    @GetMapping(value = "/modify/{id}")
    public String editAddress(@PathVariable Long id,
                              Model model) {
        model.addAttribute("address", addressService.getOneAddress(id));
        model.addAttribute("errors", new HashMap<>());
        model.addAttribute("errorsGlobal", new ArrayList<>());
        return "addresses/modify";
    }

    @PostMapping(value = "/modify")
    public String editAddress(@Valid @ModelAttribute AddressDTO addressDTO,
                              BindingResult bindingResult,
                              Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("address", addressDTO);
            model.addAttribute("errors", bindingResult
                    .getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getCode)));
            model.addAttribute("errorsGlobal", bindingResult
                    .getGlobalErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList()));
            return "addresses/modify";
        }
        addressService.modifyAddress(addressDTO);
        return "redirect:/addresses";
    }

    @GetMapping
    public String getAllAddresses(Model model) {
        model.addAttribute("addresses", addressService.getAllAddresses());
        return "addresses/all";
    }

    @GetMapping("/{id}")
    public String getOneAddress(@PathVariable Long id,
                                Model model) {
        model.addAttribute("address", addressService.getOneAddress(id));
        return "addresses/one";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/delete")
    public String deleteAddress(@RequestParam Long id) {
        addressService.deleteAddress(id);
        return "redirect:/addresses";
    }
}
