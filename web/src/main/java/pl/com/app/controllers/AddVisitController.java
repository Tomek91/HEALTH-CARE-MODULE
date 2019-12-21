package pl.com.app.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.com.app.dto.DoctorDTO;
import pl.com.app.dto.VisitCriteriaDTO;
import pl.com.app.dto.VisitCriteriaHomePageDTO;
import pl.com.app.service.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/add-visit")
@RequiredArgsConstructor
public class AddVisitController {

    private final AddVisitService addVisitService;
    private final SpecializationService specializationService;
    private final OpinionService opinionService;
    private final VisitService visitService;
    private final NationalityService nationalityService;


    @GetMapping("/criteria")
    public String visitCriteria(Model model) {
        model.addAttribute("nationalities", nationalityService.getAllNationalities());
        model.addAttribute("visitCriteria", new VisitCriteriaDTO());
        model.addAttribute("specializations", specializationService.getAllSpecializations());
        model.addAttribute("errors", new HashMap<>());
        return "addVisit/addVisit";
    }

    @PostMapping("/criteria")
    public String visitCriteria(@Valid @ModelAttribute VisitCriteriaDTO visitCriteriaDTO,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("visitCriteria", visitCriteriaDTO);
            model.addAttribute("nationalities", nationalityService.getAllNationalities());
            model.addAttribute("specializations", specializationService.getAllSpecializations());
            model.addAttribute("errors", bindingResult
                    .getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            return "addVisit/addVisit";
        }
        redirectAttributes.addFlashAttribute("visitCriteriaDTO", visitCriteriaDTO);
        return "redirect:/add-visit/doctors-list";
    }

    @GetMapping("/doctors-list")
    public String doctorList(Model model) {
        Map<String, Object> modelMap = model.asMap();
        VisitCriteriaDTO visitCriteriaDTO = (VisitCriteriaDTO) modelMap.get("visitCriteriaDTO");
        List<DoctorDTO> doctorsByCriteria = addVisitService.findAllDoctorsByVisitCriteria(visitCriteriaDTO);
        model.addAttribute("doctors", doctorsByCriteria);
        model.addAttribute("doctorsOpinions", opinionService.getAllOpinionsBySpecifiedDoctors(doctorsByCriteria));
        model.addAttribute("doctorsSchedules", visitService.getAllSchedulesBySpecifiedDoctors(doctorsByCriteria));
        return "addVisit/doctorList";
    }

    @GetMapping("/home-page/doctors-list")
    public String doctorListHomePage(Model model) {
        Map<String, Object> modelMap = model.asMap();
        VisitCriteriaHomePageDTO visitCriteriaHomePageDTO = (VisitCriteriaHomePageDTO) modelMap.get("visitCriteriaHomePageDTO");
        List<DoctorDTO> doctorsByCriteria = addVisitService.findAllDoctorsByVisitCriteria(visitCriteriaHomePageDTO);
        model.addAttribute("doctors", doctorsByCriteria);
        model.addAttribute("doctorsOpinions", opinionService.getAllOpinionsBySpecifiedDoctors(doctorsByCriteria));
        model.addAttribute("doctorsSchedules", visitService.getAllSchedulesBySpecifiedDoctors(doctorsByCriteria));
        return "addVisit/doctorList";
    }
        
    @PostMapping("/date-time")
    public String addVisitWithDateTime(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time,
                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                       @RequestParam Long doctorId,
                                       RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("time", time);
        redirectAttributes.addFlashAttribute("date", date);
        redirectAttributes.addFlashAttribute("doctorId", doctorId);
        return "redirect:/visits/add";
    }
}
