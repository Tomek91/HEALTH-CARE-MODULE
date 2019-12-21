package pl.com.app.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.com.app.dto.DoctorDTO;
import pl.com.app.dto.PatientDTO;
import pl.com.app.dto.RoleDTO;
import pl.com.app.dto.UserDTO;
import pl.com.app.exceptions.ExceptionCode;
import pl.com.app.exceptions.MyException;
import pl.com.app.model.EGender;
import pl.com.app.service.*;
import pl.com.app.validators.AdminValidator;
import pl.com.app.validators.DoctorValidator;
import pl.com.app.validators.PatientValidator;
import pl.com.app.validators.RoleValidator;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;
    private final RoleService roleService;
    private final WorkplaceService workplaceService;
    private final NationalityService nationalityService;
    private final SpecializationService specializationService;
    private final DoctorValidator doctorValidator;
    private final PatientValidator patientValidator;
    private final AdminValidator adminValidator;
    private final RoleValidator roleValidator;


    @InitBinder("userDTO")
    private void initAdminBinder(WebDataBinder binder) {
        binder.setValidator(adminValidator);
    }

    @InitBinder("doctorDTO")
    private void initDoctorBinder(WebDataBinder binder) {
        binder.setValidator(doctorValidator);
    }

    @InitBinder("patientDTO")
    private void initPatientBinder(WebDataBinder binder) {
        binder.setValidator(patientValidator);
    }

    @InitBinder("roleDTO")
    private void initRoleBinder(WebDataBinder binder) {
        binder.setValidator(roleValidator);
    }


    @GetMapping("/choose-role")
    public String chooseRole(Model model) {
        model.addAttribute("role", new RoleDTO());
        model.addAttribute("roles", roleService
                .getAllRoles()
                .stream()
                .peek(x -> x.setName(x.getName().replaceAll("USER_", "")))
                .sorted(Comparator.comparing(RoleDTO::getName))
                .collect(Collectors.toList())
        );
        model.addAttribute("errors", new ArrayList<>());

        return "registration/chooseRole";
    }

    @PostMapping("/choose-role")
    public String chooseRole(@Valid @ModelAttribute(name = "roleDTO") RoleDTO roleDTO,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("role", roleDTO);
            model.addAttribute("roles", roleService
                    .getAllRoles()
                    .stream()
                    .peek(x -> x.setName(x.getName().replaceAll("USER_", "")))
                    .sorted(Comparator.comparing(RoleDTO::getName))
                    .collect(Collectors.toList())
            );
            model.addAttribute("errors", bindingResult
                    .getFieldErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList()));
            return "registration/chooseRole";
        }

        redirectAttributes.addFlashAttribute("roleId", roleDTO.getId());
        RoleDTO oneRole = roleService.getOneRole(roleDTO.getId());
        String redirectView;
        if (oneRole.getName().equals("USER_DOCTOR")) {
            redirectView = "registration/add-doctor";
        } else if (oneRole.getName().equals("USER_PATIENT")) {
            redirectView = "registration/add-patient";
        } else {
            redirectView = "registration/add-admin";
        }
        return "redirect:/" + redirectView;
    }

    @GetMapping("/add-admin")
    public String addAdmin(Model model) {
        Map<String, Object> modelAttr = model.asMap();
        Long roleId = (Long) modelAttr.get("roleId");
        if (roleId == null) {
            throw new MyException(ExceptionCode.ACCESS_DENIED, "ROLE ID IS NULL");
        }
        model.addAttribute("user", UserDTO.builder().roleDTO(RoleDTO.builder().id(roleId).build()).build());
        model.addAttribute("errors", new HashMap<>());
        model.addAttribute("errorsGlobal", new ArrayList<>());
        return "registration/registrationAdmin";
    }

    @PostMapping("/add-admin")
    public String addAdmin(@Valid @ModelAttribute(name = "userDTO") UserDTO userDTO,
                           BindingResult bindingResult,
                           Model model,
                           HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userDTO);
            model.addAttribute("errors", bindingResult
                    .getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getCode)));
            model.addAttribute("errorsGlobal", bindingResult
                    .getGlobalErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList()));
            return "registration/registrationAdmin";
        }
        registrationService.registerNewAdmin(userDTO, request);
        return "redirect:/index";
    }

    @GetMapping("/add-doctor")
    public String addDoctor(Model model) {
        Map<String, Object> modelAttr = model.asMap();
        Long roleId = (Long) modelAttr.get("roleId");
        if (roleId == null) {
            throw new MyException(ExceptionCode.ACCESS_DENIED, "ROLE ID IS NULL");
        }
        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setRoleDTO(RoleDTO.builder().id(roleId).build());
        model.addAttribute("doctor", doctorDTO);
        model.addAttribute("genders", EGender.values());
        model.addAttribute("nationalities", nationalityService.getAllNationalities());
        model.addAttribute("workplaces", workplaceService.getAllWorkplaces());
        model.addAttribute("specializations", specializationService.getAllSpecializations());
        model.addAttribute("errors", new HashMap<>());
        model.addAttribute("errorsGlobal", new ArrayList<>());
        return "registration/registrationDoctor";
    }

    @PostMapping("/add-doctor")
    public String addDoctor(@Valid @ModelAttribute(name = "doctorDTO") DoctorDTO doctorDTO,
                            BindingResult bindingResult,
                            Model model,
                            HttpServletRequest request) {
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
            return "registration/registrationDoctor";
        }
        registrationService.registerNewDoctor(doctorDTO, request);
        return "redirect:/index";
    }

    @GetMapping("/add-patient")
    public String addPatient(Model model) {
        Map<String, Object> modelAttr = model.asMap();
        Long roleId = (Long) modelAttr.get("roleId");
        if (roleId == null) {
            throw new MyException(ExceptionCode.ACCESS_DENIED, "ROLE ID IS NULL");
        }
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setRoleDTO(RoleDTO.builder().id(roleId).build());
        model.addAttribute("patient", patientDTO);
        model.addAttribute("genders", EGender.values());
        model.addAttribute("nationalities", nationalityService.getAllNationalities());
        model.addAttribute("errors", new HashMap<>());
        model.addAttribute("errorsGlobal", new ArrayList<>());
        return "registration/registrationPatient";
    }

    @PostMapping("/add-patient")
    public String addPatient(@Valid @ModelAttribute(name = "patientDTO") PatientDTO patientDTO,
                             BindingResult bindingResult,
                             Model model,
                             HttpServletRequest request) {
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
            return "registration/registrationPatient";
        }
        registrationService.registerNewPatient(patientDTO, request);
        return "redirect:/index";
    }

    @GetMapping("/registerConfirmation")
    public String registrationConfirmation(@RequestParam String token) {
        registrationService.confirmRegistration(token);
        return "redirect:/login";
    }
}
