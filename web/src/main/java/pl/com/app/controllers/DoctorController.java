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
import pl.com.app.authentication.AuthenticationFacade;
import pl.com.app.dto.DoctorDTO;
import pl.com.app.dto.UserDTO;
import pl.com.app.exceptions.AccessDeniedException;
import pl.com.app.exceptions.ExceptionCode;
import pl.com.app.model.EGender;
import pl.com.app.service.*;
import pl.com.app.validators.DoctorValidator;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;
    private final AdviceService adviceService;
    private final NationalityService nationalityService;
    private final SpecializationService specializationService;
    private final OpinionService opinionService;
    private final VisitService visitService;
    private final PatientCardDiseaseService patientCardDiseaseService;
    private final WorkplaceService workplaceService;
    private final DoctorValidator doctorValidator;
    private final AuthenticationFacade authenticationFacade;


    @InitBinder
    private void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.setValidator(doctorValidator);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/add")
    public String addDoctor(Model model) {
        model.addAttribute("doctor", new DoctorDTO());
        model.addAttribute("genders", EGender.values());
        model.addAttribute("nationalities", nationalityService.getAllNationalities());
        model.addAttribute("workplaces", workplaceService.getAllWorkplaces());
        model.addAttribute("specializations", specializationService.getAllSpecializations());
        model.addAttribute("errors", new HashMap<>());
        model.addAttribute("errorsGlobal", new ArrayList<>());
        return "doctors/add";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public String addDoctor(@Valid @ModelAttribute DoctorDTO doctorDTO,
                            BindingResult bindingResult,
                            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("doctor", doctorDTO);
            model.addAttribute("genders", EGender.values());
            model.addAttribute("nationalities", nationalityService.getAllNationalities());
            model.addAttribute("workplaces", workplaceService.getAllWorkplaces());
            model.addAttribute("specializations", specializationService.getAllSpecializations());
            model.addAttribute("errors", bindingResult
                    .getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getCode)));
            model.addAttribute("errorsGlobal", bindingResult
                    .getGlobalErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList()));
            return "doctors/add";
        }
        doctorService.addDoctor(doctorDTO);
        return "redirect:/doctors";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/modify/{id}")
    public String editDoctor(@PathVariable Long id,
                             Model model) {

        UserDTO loggedUser = authenticationFacade.getLoggedUser();
        if (!loggedUser.getId().equals(id)){
            throw new AccessDeniedException(ExceptionCode.ACCESS_DENIED, "You can't modify user id: " + id);
        }

        model.addAttribute("doctor", doctorService.getOneDoctor(id));
        model.addAttribute("genders", EGender.values());
        model.addAttribute("nationalities", nationalityService.getAllNationalities());
        model.addAttribute("workplaces", workplaceService.getAllWorkplaces());
        model.addAttribute("specializations", specializationService.getAllSpecializations());
        model.addAttribute("errors", new HashMap<>());
        model.addAttribute("errorsGlobal", new ArrayList<>());
        return "doctors/modify";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/modify")
    public String editDoctor(@Valid @ModelAttribute DoctorDTO doctorDTO,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasFieldErrors()) {
            model.addAttribute("doctor", doctorDTO);
            model.addAttribute("genders", EGender.values());
            model.addAttribute("nationalities", nationalityService.getAllNationalities());
            model.addAttribute("workplaces", workplaceService.getAllWorkplaces());
            model.addAttribute("specializations", specializationService.getAllSpecializations());
            model.addAttribute("errors", bindingResult
                    .getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getCode)));
            model.addAttribute("errorsGlobal", bindingResult
                    .getGlobalErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList()));
            return "doctors/modify";
        }
        doctorService.modifyDoctor(doctorDTO);
        return "redirect:/doctors";
    }

    @GetMapping
    public String getAllDoctors(Model model) {
        model.addAttribute("doctors", doctorService.getAllDoctors());
        model.addAttribute("doctorsOpinions", opinionService.getAllOpinionsByDoctors());
        model.addAttribute("doctorsSchedules", visitService.getAllSchedulesByDoctors());
        return "doctors/all";
    }

    @GetMapping("/{id}")
    public String getOneDoctor(@PathVariable Long id,
                               Model model) {
        model.addAttribute("doctor", doctorService.getOneDoctor(id));
        return "doctors/one";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/delete")
    public String deleteDoctor(@RequestParam Long id) {
        doctorService.deleteDoctor(id);
        return "redirect:/doctors";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        UserDTO loggedUser = authenticationFacade.getLoggedUser();
        DoctorDTO doctorDTO = doctorService.getOneDoctor(loggedUser.getId());

        model.addAttribute("patientCards", patientCardDiseaseService.findAllPatientCardDiseasesByDoctor(loggedUser.getId()));
        model.addAttribute("opinions", opinionService.getOpinionsByDoctor(loggedUser.getId()));
        model.addAttribute("advices", adviceService.getAdvicesByDoctor(loggedUser.getId()));
        model.addAttribute("doctor", doctorDTO);

        return "doctors/dashboard";
    }

}
