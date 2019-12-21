package pl.com.app.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.com.app.authentication.AuthenticationFacade;
import pl.com.app.dto.PatientCardDTO;
import pl.com.app.dto.UserDTO;
import pl.com.app.model.EImportance;
import pl.com.app.service.DoctorVisitService;
import pl.com.app.service.PatientCardDiseaseService;
import pl.com.app.service.SpecializationService;
import pl.com.app.service.VisitService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/doctor-visits")
@RequiredArgsConstructor
public class DoctorVisitController {

    private final DoctorVisitService doctorVisitService;
    private final VisitService visitService;
    private final SpecializationService specializationService;
    private final PatientCardDiseaseService patientCardDiseaseService;
    private final AuthenticationFacade authenticationFacade;


    @GetMapping
    public String getAllVisits(Model model) {
        UserDTO doctor = authenticationFacade.getLoggedUser();
        model.addAttribute("visits", doctorVisitService.getAllVisitsForDoctor(doctor.getId()));
        return "doctorVisit/all";
    }

    @PostMapping("/add/new/{visitId}")
    public String startNewVisit(@PathVariable Long visitId,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        redirectAttributes.addFlashAttribute("visitId", visitId);
        return "redirect:/doctor-visits/add";
    }

    @GetMapping("/add")
    public String startVisit(Model model) {
        Map<String, Object> modelMap = model.asMap();
        Long visitId = (Long)modelMap.get("visitId");

        PatientCardDTO patientCardDTO = doctorVisitService.getPatientCardByVisitId(visitId);
        model.addAttribute("patientCard", patientCardDTO);
        model.addAttribute("importances", EImportance.values());
        model.addAttribute("patientCards", patientCardDiseaseService.findAllPatientCardDiseasesForPatient(patientCardDTO.getPatientDTO().getId()));
        model.addAttribute("errors", new HashMap<>());
        model.addAttribute("visitId", visitId);
        return "doctorVisit/visit";
    }

    @PostMapping("/add")
    public String startVisit(@Valid @ModelAttribute PatientCardDTO patientCardDTO,
                             @RequestParam Long visitId,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("patientCard", patientCardDTO);
            model.addAttribute("importances", EImportance.values());
            model.addAttribute("patientCards", patientCardDiseaseService.findAllPatientCardDiseasesForPatient(patientCardDTO.getPatientDTO().getId()));
            model.addAttribute("errors", bindingResult
                    .getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            return "doctorVisit/visit";
        }
        patientCardDiseaseService.addPatientCardDisease(patientCardDTO);
        visitService.setEndedVisit(visitId);
        return "redirect:/doctor-visits";
    }


}
