package pl.com.app.validators;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pl.com.app.dto.DoctorDTO;
import pl.com.app.exceptions.ExceptionCode;
import pl.com.app.exceptions.MyException;
import pl.com.app.service.DoctorService;
import pl.com.app.service.UserService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class DoctorValidator implements Validator {
    private final DoctorService doctorService;
    private final UserService userService;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(DoctorDTO.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        if (o == null) {
            throw new MyException(ExceptionCode.VALIDATION, "OBJECT IS NULL");
        }

        DoctorDTO doctorDTO = (DoctorDTO) o;

        if (StringUtils.isBlank(doctorDTO.getName()) || !doctorDTO.getName().matches("[A-Z ]+")) {
            errors.rejectValue("name", "Name is not correct");
        }
        if (StringUtils.isBlank(doctorDTO.getSurname()) || !doctorDTO.getSurname().matches("[A-Za-z ]+")) {
            errors.rejectValue("surname", "Surname is not correct");
        }
        if (doctorDTO.getBirthday() == null || doctorDTO.getBirthday().isAfter(LocalDate.now())) {
            errors.rejectValue("birthday", "Birthday is not correct");
        }
        if (StringUtils.isBlank(doctorDTO.getUserName())) {
            errors.rejectValue("userName", "UserName is not correct");
        }
        if (StringUtils.isBlank(doctorDTO.getPassword())) {
            errors.rejectValue("password", "Password is not correct");
        }
        if (StringUtils.isBlank(doctorDTO.getPassword())) {
            errors.rejectValue("passwordConfirmation", "Password confirmation is not correct");
        }
        if (StringUtils.isBlank(doctorDTO.getEmail()) || !doctorDTO.getEmail().matches(".+@\\w+.pl")) {
            errors.rejectValue("email", "Email is not correct");
        }
        if (doctorDTO.getCost() == null || doctorDTO.getCost().compareTo(BigDecimal.ZERO) < 0) {
            errors.rejectValue("cost", "Cost is not correct");
        }

        if (!errors.hasErrors()) {
            if (doctorService.isNotUniqueNameAndSurname(doctorDTO.getName(), doctorDTO.getSurname())) {
                errors.reject("name_surname_unique", "NAME AND SURNAME MUST BE UNIQUE");
            }
            if (userService.findByEmail(doctorDTO.getEmail()).isPresent()){
                errors.reject("email_unique", "EMAIL ALREADY EXISTS");
            }
            if (userService.findByUserName(doctorDTO.getUserName()).isPresent()){
                errors.reject("email_unique", "USERNAME ALREADY EXISTS");
            }
            if (!Objects.equals(doctorDTO.getPassword(), doctorDTO.getPasswordConfirmation())){
                errors.reject("passwords_same", "PASSWORDS ARE NOT THE SAME");
            }
        }
    }
}
