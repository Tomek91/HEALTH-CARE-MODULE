package pl.com.app.validators;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pl.com.app.dto.PatientDTO;
import pl.com.app.exceptions.ExceptionCode;
import pl.com.app.exceptions.MyException;
import pl.com.app.service.PatientService;
import pl.com.app.service.UserService;

import java.time.LocalDate;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class PatientValidator implements Validator {
    private final PatientService patientService;
    private final UserService userService;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(PatientDTO.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        if (o == null) {
            throw new MyException(ExceptionCode.VALIDATION, "OBJECT IS NULL");
        }

        PatientDTO patientDTO = (PatientDTO) o;

        if (StringUtils.isBlank(patientDTO.getName()) || !patientDTO.getName().matches("[A-Z ]+")) {
            errors.rejectValue("name", "Name is not correct");
        }
        if (StringUtils.isBlank(patientDTO.getSurname()) || !patientDTO.getSurname().matches("[A-Za-z ]+")) {
            errors.rejectValue("surname", "Surname is not correct");
        }
        if (patientDTO.getBirthday() == null || patientDTO.getBirthday().isAfter(LocalDate.now())) {
            errors.rejectValue("birthday", "Birthday is not correct");
        }
        if (StringUtils.isBlank(patientDTO.getUserName())) {
            errors.rejectValue("userName", "UserName is not correct");
        }
        if (StringUtils.isBlank(patientDTO.getPassword())) {
            errors.rejectValue("password", "Password is not correct");
        }
        if (StringUtils.isBlank(patientDTO.getPassword())) {
            errors.rejectValue("passwordConfirmation", "Password confirmation is not correct");
        }
        if (StringUtils.isBlank(patientDTO.getEmail()) || !patientDTO.getEmail().matches(".+@\\w+.pl")) {
            errors.rejectValue("email", "Email is not correct");
        }
        if (StringUtils.isBlank(patientDTO.getInsuranceNumber())) {
            errors.rejectValue("insuranceNumber", "InsuranceNumber is not correct");
        }

        if (!errors.hasErrors()) {
            if (patientService.isNotUniqueNameAndSurname(patientDTO.getName(), patientDTO.getSurname())) {
                errors.reject("name_surname_unique", "NAME AND SURNAME MUST BE UNIQUE");
            }
            if (userService.findByEmail(patientDTO.getEmail()).isPresent()){
                errors.reject("email_unique", "EMAIL ALREADY EXISTS");
            }
            if (userService.findByUserName(patientDTO.getUserName()).isPresent()){
                errors.reject("email_unique", "USERNAME ALREADY EXISTS");
            }
            if (!Objects.equals(patientDTO.getPassword(), patientDTO.getPasswordConfirmation())){
                errors.reject("passwords_same", "PASSWORDS ARE NOT THE SAME");
            }
        }
    }
}
