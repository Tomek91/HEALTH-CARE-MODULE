package pl.com.app.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.com.app.authentication.AuthenticationFacade;
import pl.com.app.dto.AdviceAnswerDTO;
import pl.com.app.dto.AdviceDTO;
import pl.com.app.dto.SpecializationDTO;
import pl.com.app.dto.UserDTO;
import pl.com.app.service.AdviceService;
import pl.com.app.service.DoctorService;
import pl.com.app.service.SpecializationService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/advices")
@RequiredArgsConstructor
public class AdviceController {

    private final AdviceService adviceService;
    private final SpecializationService specializationService;
    private final DoctorService doctorService;
    private final AuthenticationFacade authenticationFacade;


    @GetMapping("/add")
    public String addAdvice(Model model) {
        List<SpecializationDTO> specializations = specializationService.getAllSpecializations();
        specializations.add(0, new SpecializationDTO());
        model.addAttribute("advice", new AdviceDTO());
        model.addAttribute("specializations", specializations);
        model.addAttribute("errors", new HashMap<>());
        return "advices/add";
    }

    @PostMapping("/add")
    public String addAdvice(@Valid @ModelAttribute AdviceDTO adviceDTO,
                            BindingResult bindingResult,
                            Model model) {
        List<SpecializationDTO> specializations = specializationService.getAllSpecializations();
        specializations.add(0, new SpecializationDTO());
        if (bindingResult.hasErrors()) {
            model.addAttribute("advice", adviceDTO);
            model.addAttribute("specializations", specializations);
            model.addAttribute("errors", bindingResult
                    .getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            return "advices/add";
        }
        adviceService.addAdvice(adviceDTO);
        return "redirect:/advices";
    }

    @GetMapping
    public String getAllAdvices(Model model) {
        model.addAttribute("advices", adviceService.getAllAdvices());
        return "advices/all";
    }

    @GetMapping("/{id}")
    public String getOneAdvice(@PathVariable Long id,
                               Model model) {
        model.addAttribute("advice", adviceService.getOneAdvice(id));
        return "advices/one";
    }

    @PostMapping("/delete")
    public String deleteAdvice(@RequestParam Long id) {
        adviceService.deleteAdvice(id);
        return "redirect:/advices";
    }

    @GetMapping("/not-answer")
    public String getAllNotAnswerAdvicesByDoctor(Model model) {
        UserDTO doctor = authenticationFacade.getLoggedUser();
        model.addAttribute("advices", adviceService.getAllNotAnswerAdvicesByDoctor(doctor.getId()));
        return "advices/allNotAnswer";
    }

    @PostMapping("/answer/to-user/{id}")
    public String addAnswerToUser(@PathVariable Long id,
                                  RedirectAttributes redirectAttributes,
                                  Model model) {
        redirectAttributes.addFlashAttribute("adviceId", id);
        return "redirect:/advices/answer";
    }

    @GetMapping("/answer")
    public String addAnswerAdvice(Model model) {
        Map<String, Object> modelMap = model.asMap();
        Long adviceId = (Long)modelMap.get("adviceId");

        AdviceAnswerDTO oneAdviceAnswer = adviceService.getOneAdviceAnswer(adviceId);
        model.addAttribute("advice", oneAdviceAnswer);
        model.addAttribute("specializations", oneAdviceAnswer.getSpecializationDTO());
        model.addAttribute("doctor", oneAdviceAnswer.getDoctorDTO());
        model.addAttribute("errors", new HashMap<>());
        return "advices/answer";
    }

    @PostMapping("/answer")
    public String addAnswerAdvice(@Valid @ModelAttribute AdviceAnswerDTO adviceAnswerDTO,
                                  BindingResult bindingResult,
                                  Model model,
                                  HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("advice", adviceAnswerDTO);
            model.addAttribute("specializations", specializationService.getOneSpecialization(adviceAnswerDTO.getSpecializationDTO().getId()));
            model.addAttribute("doctor", doctorService.getOneDoctor(adviceAnswerDTO.getDoctorDTO().getId()));
            model.addAttribute("errors", bindingResult
                    .getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            return "advices/answer";
        }
        adviceService.addAnswerAdvice(adviceAnswerDTO, request);
        return "redirect:/advices";
    }

    @GetMapping("/not-answer-adm")
    public String getAllNotAnswerAdmAdvices(Model model) {
        model.addAttribute("advices", adviceService.getAllNotAnswerAdmAdvices());
        return "advices/allNotAnswerAdm";
    }

    @PostMapping("/answer-adm/to-user/{id}")
    public String addAnswerAdmToUser(@PathVariable Long id,
                                     RedirectAttributes redirectAttributes,
                                     Model model) {
        redirectAttributes.addFlashAttribute("adviceId", id);
        return "redirect:/advices/answer-adm";
    }

    @GetMapping("/answer-adm")
    public String addAnswerAdmAdvice(Model model) {
        Map<String, Object> modelMap = model.asMap();
        Long adviceId = (Long)modelMap.get("adviceId");

        AdviceAnswerDTO oneAdviceAnswer = adviceService.getOneAdviceAnswer(adviceId);
        model.addAttribute("advice", oneAdviceAnswer);
        model.addAttribute("errors", new HashMap<>());
        return "advices/answerAdm";
    }

    @PostMapping("/answer-adm")
    public String addAnswerAdmAdvice(@Valid @ModelAttribute AdviceAnswerDTO adviceAnswerDTO,
                                     BindingResult bindingResult,
                                     Model model,
                                     HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("advice", adviceAnswerDTO);
            model.addAttribute("errors", bindingResult
                    .getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            return "advices/answerAdm";
        }
        adviceService.addAnswerAdvice(adviceAnswerDTO, request);
        return "redirect:/advices";
    }
}
