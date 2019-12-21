package pl.com.app.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import pl.com.app.authentication.AuthenticationFacade;
import pl.com.app.dto.PatientDTO;
import pl.com.app.dto.UserDTO;
import pl.com.app.exceptions.AccessDeniedException;
import pl.com.app.exceptions.ExceptionCode;
import pl.com.app.model.EGender;
import pl.com.app.service.*;
import pl.com.app.validators.PatientValidator;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final NationalityService nationalityService;
    private final PatientService patientService;
    private final AdviceService adviceService;
    private final OpinionService opinionService;
    private final PatientCardDiseaseService patientCardDiseaseService;
    private final AuthenticationFacade authenticationFacade;
    private final PatientValidator patientValidator;

    @InitBinder
    private void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.setValidator(patientValidator);
    }

    @GetMapping("/add")
    public String addPatient(Model model) {
        model.addAttribute("patient", new PatientDTO());
        model.addAttribute("genders", EGender.values());
        model.addAttribute("nationalities", nationalityService.getAllNationalities());
        model.addAttribute("errors", new HashMap<>());
        model.addAttribute("errorsGlobal", new ArrayList<>());
        return "patients/add";
    }

    @PostMapping("/add")
    public String addPatient(@Valid @ModelAttribute PatientDTO patientDTO,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("patient", patientDTO);
            model.addAttribute("genders", EGender.values());
            model.addAttribute("nationalities", nationalityService.getAllNationalities());
            model.addAttribute("errors", bindingResult
                    .getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getCode)));
            model.addAttribute("errorsGlobal", bindingResult
                    .getGlobalErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList()));
            return "patients/add";
        }
        patientService.addPatient(patientDTO);
        return "redirect:/patients";
    }

    @GetMapping(value = "/modify/{id}")
    public String editPatient(@PathVariable Long id,
                              Model model) {
        UserDTO loggedUser = authenticationFacade.getLoggedUser();
        if (!loggedUser.getId().equals(id)){
            throw new AccessDeniedException(ExceptionCode.ACCESS_DENIED, "You can't modify user id: " + id);
        }

        model.addAttribute("patient", patientService.getOnePatient(id));
        model.addAttribute("genders", EGender.values());
        model.addAttribute("nationalities", nationalityService.getAllNationalities());
        model.addAttribute("errors", new HashMap<>());
        model.addAttribute("errorsGlobal", new ArrayList<>());
        return "patients/modify";
    }

    @PostMapping(value = "/modify")
    public String editPatient(@Valid @ModelAttribute PatientDTO patientDTO,
                              BindingResult bindingResult,
                              Model model) {
        if (bindingResult.hasFieldErrors()) {
            model.addAttribute("patient", patientDTO);
            model.addAttribute("genders", EGender.values());
            model.addAttribute("nationalities", nationalityService.getAllNationalities());
            model.addAttribute("errors", bindingResult
                    .getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getCode)));
            model.addAttribute("errorsGlobal", bindingResult
                    .getGlobalErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList()));
            return "patients/modify";
        }
        patientService.modifyPatient(patientDTO);
        return "redirect:/patients";
    }

    @GetMapping
    public String getAllPatients(Model model) {
        model.addAttribute("patients", patientService.getAllPatients());
        return "patients/all";
    }

    @GetMapping("/{id}")
    public String getOnePatient(@PathVariable Long id,
                                Model model) {
        model.addAttribute("patient", patientService.getOnePatient(id));
        return "patients/one";
    }

    @PostMapping("/delete")
    public String deletePatient(@RequestParam Long id) {
        patientService.deletePatient(id);
        return "redirect:/patients";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        UserDTO loggedUser = authenticationFacade.getLoggedUser();
        PatientDTO patientDTO = patientService.getOnePatient(loggedUser.getId());

        model.addAttribute("patientCards", patientCardDiseaseService.findAllPatientCardDiseasesForPatient(loggedUser.getId()));
        model.addAttribute("opinions", opinionService.getOpinionsByPatient(loggedUser.getId()));
        model.addAttribute("sortedDoctors", adviceService.getDoctorsSortedByOpinions());
        model.addAttribute("patient", patientDTO);

        return "patients/dashboard";
    }

}
