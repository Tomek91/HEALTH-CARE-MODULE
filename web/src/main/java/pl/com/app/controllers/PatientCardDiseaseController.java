package pl.com.app.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import pl.com.app.dto.PatientCardDTO;
import pl.com.app.model.EImportance;
import pl.com.app.service.DoctorService;
import pl.com.app.service.PatientCardDiseaseService;
import pl.com.app.service.PatientService;
import pl.com.app.service.SpecializationService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/patient-cards-disease")
@RequiredArgsConstructor
public class PatientCardDiseaseController {

    private final PatientCardDiseaseService patientCardDiseaseService;
    private final PatientService patientService;
    private final SpecializationService specializationService;
    private final DoctorService doctorService;


    @GetMapping("/add")
    public String addPatientCardDisease(Model model) {
        model.addAttribute("patientCardDisease", new PatientCardDTO());
        model.addAttribute("patients", patientService.getAllPatients());
        model.addAttribute("doctors", doctorService.getAllDoctors());
        model.addAttribute("specializations", specializationService.getAllSpecializations());
        model.addAttribute("importances", EImportance.values());
        model.addAttribute("errors", new HashMap<>());
        return "workplaces/add";
    }

    @PostMapping("/add")
    public String addPatientCardDisease(@Valid @ModelAttribute PatientCardDTO patientCardDTO,
                                        BindingResult bindingResult,
                                        Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("patientCardDisease", patientCardDTO);
            model.addAttribute("patients", patientService.getAllPatients());
            model.addAttribute("doctors", doctorService.getAllDoctors());
            model.addAttribute("specializations", specializationService.getAllSpecializations());
            model.addAttribute("importances", EImportance.values());
            model.addAttribute("errors", bindingResult
                    .getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            return "patientCardsDisease/add";
        }
        patientCardDiseaseService.addPatientCardDisease(patientCardDTO);
        return "redirect:/patientCardsDisease";
    }

    @GetMapping(value = "/modify/{id}")
    public String editPatientCardDisease(@PathVariable Long id,
                                         Model model) {
        model.addAttribute("patientCardDisease", patientCardDiseaseService.getOnePatientCardDisease(id));
        model.addAttribute("patients", patientService.getAllPatients());
        model.addAttribute("doctors", doctorService.getAllDoctors());
        model.addAttribute("specializations", specializationService.getAllSpecializations());
        model.addAttribute("importances", EImportance.values());
        model.addAttribute("errors", new HashMap<>());
        return "patientCardsDisease/modify";
    }

    @PostMapping(value = "/modify")
    public String editPatientCardDisease(@Valid @ModelAttribute PatientCardDTO patientCardDTO,
                                         BindingResult bindingResult,
                                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("patientCardDisease", patientCardDTO);
            model.addAttribute("patients", patientService.getAllPatients());
            model.addAttribute("doctors", doctorService.getAllDoctors());
            model.addAttribute("specializations", specializationService.getAllSpecializations());
            model.addAttribute("importances", EImportance.values());
            model.addAttribute("errors", bindingResult
                    .getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            return "patientCardsDisease/modify";
        }
        patientCardDiseaseService.modifyPatientCardDisease(patientCardDTO);
        return "redirect:/patientCardsDisease";
    }

    @GetMapping
    public String getAllPatientCardDiseases(Model model) {
        model.addAttribute("patientCardDiseases", patientCardDiseaseService.getAllPatientCardDiseases());
        return "patientCardsDisease/all";
    }

    @GetMapping("/{id}")
    public String getOnePatientCardDisease(@PathVariable Long id,
                                           Model model) {
        model.addAttribute("patientCardDisease", patientCardDiseaseService.getOnePatientCardDisease(id));
        return "patientCardsDisease/one";
    }

    @PostMapping("/delete")
    public String deletePatientCardDisease(@RequestParam Long id) {
        patientCardDiseaseService.deletePatientCardDisease(id);
        return "redirect:/patientCardsDisease";
    }
}
