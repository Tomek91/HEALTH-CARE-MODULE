package pl.com.app.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import pl.com.app.authentication.AuthenticationFacade;
import pl.com.app.dto.DoctorDTO;
import pl.com.app.dto.PatientDTO;
import pl.com.app.dto.UserDTO;
import pl.com.app.dto.VisitDTO;
import pl.com.app.exceptions.AccessDeniedException;
import pl.com.app.exceptions.ExceptionCode;
import pl.com.app.parsers.FileNames;
import pl.com.app.service.DoctorService;
import pl.com.app.service.FileService;
import pl.com.app.service.PatientService;
import pl.com.app.service.VisitService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/visits")
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;
    private final FileService fileService;
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final AuthenticationFacade authenticationFacade;

    @GetMapping("/add")
    public String addVisits(Model model) {
        Map<String, Object> modelMap = model.asMap();
        LocalDate choseDate = (LocalDate) modelMap.get("date");
        LocalTime choseTime = (LocalTime) modelMap.get("time");
        Long doctorId = (Long) modelMap.get("doctorId");

        DoctorDTO doctorDTO = doctorService.getOneDoctor(doctorId);
        model.addAttribute("visit", VisitDTO.builder().date(choseDate).time(choseTime).cost(doctorDTO.getCost()).build());
        model.addAttribute("doctors", doctorDTO);
        model.addAttribute("errors", new HashMap<>());
        return "visits/add";
    }

    @PostMapping("/add")
    public String addVisits(@Valid @ModelAttribute VisitDTO visitDTO,
                            BindingResult bindingResult,
                            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("visit", visitDTO);
            model.addAttribute("doctors", doctorService.getOneDoctor(visitDTO.getDoctorDTO().getId()));
            model.addAttribute("errors", bindingResult
                    .getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            return "visits/add";
        }

        if (authenticationFacade.isUserLoggedIn()) {
            UserDTO patient = authenticationFacade.getLoggedUser();
            PatientDTO patientDTO = new PatientDTO();
            patientDTO.setId(patient.getId());
            visitDTO.setPatientDTO(patientDTO);
            visitService.addVisit(visitDTO);
            return "redirect:/visits";
        } else {
            fileService.saveVisitDataToJson(visitDTO);
            return "redirect:/resume-add";
        }
    }


    @PreAuthorize("hasRole('ROLE_USER_PATIENT')")
    @GetMapping("/resume-add")
    public String resumeAddVisits(Model model) {

        if (!fileService.isFileVisitDataExists()){
            throw new AccessDeniedException(ExceptionCode.ACCESS_DENIED, "You can't be here");
        }
        VisitDTO visitDataToJson = fileService.getVisitDataToJson();
        model.addAttribute("visit", visitDataToJson);
        model.addAttribute("doctors", doctorService.getOneDoctor(visitDataToJson.getDoctorDTO().getId()));
        model.addAttribute("errors", new HashMap<>());
        return "visits/resumeAdd";
    }

    @PreAuthorize("hasRole('ROLE_USER_PATIENT')")
    @PostMapping("/resume-add")
    public String resumeAddVisits(@Valid @ModelAttribute VisitDTO visitDTO,
                                  BindingResult bindingResult,
                                  Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("visit", visitDTO);
            model.addAttribute("doctors", doctorService.getOneDoctor(visitDTO.getDoctorDTO().getId()));
            model.addAttribute("errors", bindingResult
                    .getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            return "visits/add";
        }

        UserDTO patient = authenticationFacade.getLoggedUser();
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setId(patient.getId());
        visitDTO.setPatientDTO(patientDTO);
        visitService.addVisit(visitDTO);
        fileService.deleteFile(FileNames.VISIT_DATA);

        return "redirect:/visits";
    }

    @GetMapping(value = "/modify/{id}")
    public String editVisit(@PathVariable Long id,
                            Model model) {
        model.addAttribute("visit", visitService.getOneVisit(id));
        model.addAttribute("patients", patientService.getAllPatients());
        model.addAttribute("doctors", doctorService.getAllDoctors());
        model.addAttribute("errors", new HashMap<>());
        return "visits/modify";
    }

    @PostMapping(value = "/modify")
    public String editVisit(@Valid @ModelAttribute VisitDTO visitDTO,
                            BindingResult bindingResult,
                            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("visit", visitDTO);
            model.addAttribute("patients", patientService.getAllPatients());
            model.addAttribute("doctors", doctorService.getAllDoctors());
            model.addAttribute("errors", bindingResult
                    .getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            return "visits/modify";
        }
        visitService.modifyVisit(visitDTO);
        return "redirect:/visits";
    }

    @GetMapping
    public String getAllVisits(Model model) {
        model.addAttribute("visits", visitService.getAllVisits());
        return "visits/all";
    }

    @GetMapping("/{id}")
    public String getOneVisit(@PathVariable Long id,
                              Model model) {
        model.addAttribute("visit", visitService.getOneVisit(id));
        return "visits/one";
    }

    @PostMapping("/delete")
    public String deleteVisit(@RequestParam Long id) {
        visitService.deleteVisit(id);
        return "redirect:/visits";
    }


}
