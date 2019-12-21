package pl.com.app.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import pl.com.app.authentication.AuthenticationFacade;
import pl.com.app.dto.OpinionDTO;
import pl.com.app.service.DoctorService;
import pl.com.app.service.OpinionService;
import pl.com.app.service.PatientService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/opinions")
@RequiredArgsConstructor
public class OpinionController {

    private final OpinionService opinionService;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final AuthenticationFacade authenticationFacade;

    @GetMapping("/add")
    public String addOpinion(Model model) {
        model.addAttribute("opinion", new OpinionDTO());
        model.addAttribute("qualities", Arrays.asList(1, 2, 3, 4, 5));
        model.addAttribute("patients", authenticationFacade.getLoggedUser());
        model.addAttribute("doctors", doctorService.getAllDoctors());
        model.addAttribute("errors", new HashMap<>());
        return "opinions/add";
    }

    @PostMapping("/add")
    public String addOpinion(@Valid @ModelAttribute OpinionDTO opinionDTO,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("workplace", opinionDTO);
            model.addAttribute("qualities", Arrays.asList(1, 2, 3, 4, 5));
            model.addAttribute("patients", authenticationFacade.getLoggedUser());
            model.addAttribute("doctors", doctorService.getAllDoctors());
            model.addAttribute("errors", bindingResult
                    .getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            return "opinions/add";
        }
        opinionService.addOpinion(opinionDTO);
        return "redirect:/opinions";
    }

    @GetMapping(value = "/modify/{id}")
    public String editOpinion(@PathVariable Long id,
                              Model model) {
        model.addAttribute("opinion", opinionService.getOneOpinion(id));
        model.addAttribute("qualities", Arrays.asList(1, 2, 3, 4, 5));
        model.addAttribute("patients", patientService.getAllPatients());
        model.addAttribute("doctors", doctorService.getAllDoctors());
        model.addAttribute("errors", new HashMap<>());
        return "opinions/modify";
    }

    @PostMapping(value = "/modify")
    public String editOpinion(@Valid @ModelAttribute OpinionDTO opinionDTO,
                              BindingResult bindingResult,
                              Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("opinion", opinionDTO);
            model.addAttribute("qualities", Arrays.asList(1, 2, 3, 4, 5));
            model.addAttribute("patients", patientService.getAllPatients());
            model.addAttribute("doctors", doctorService.getAllDoctors());
            model.addAttribute("errors", bindingResult
                    .getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            return "opinions/modify";
        }
        opinionService.modifyOpinion(opinionDTO);
        return "redirect:/opinions";
    }

    @GetMapping
    public String getAllOpinions(Model model) {
        model.addAttribute("opinions", opinionService.getAllOpinions());
        return "opinions/all";
    }

    @GetMapping("/{id}")
    public String getOneOpinion(@PathVariable Long id,
                                Model model) {
        model.addAttribute("opinion", opinionService.getOneOpinion(id));
        return "opinions/one";
    }

    @PostMapping("/delete")
    public String deleteOpinion(@RequestParam Long id) {
        opinionService.deleteOpinion(id);
        return "redirect:/opinions";
    }

}
