package pl.com.app.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import pl.com.app.dto.SpecializationDTO;
import pl.com.app.exceptions.ExceptionCode;
import pl.com.app.exceptions.MyException;
import pl.com.app.service.SpecializationService;

@Component
@RequiredArgsConstructor
public class SpecializationValidator implements Validator {
    private final SpecializationService specializationService;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(SpecializationDTO.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        if (o == null) {
            throw new MyException(ExceptionCode.VALIDATION, "OBJECT IS NULL");
        }

        SpecializationDTO specializationDTO = (SpecializationDTO) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "workplace", "workplace", "WorkPlace is not correct");

        if (specializationDTO.getId() == null || !specializationService.getOneSpecialization(specializationDTO.getId()).getName().equals(specializationDTO.getName())){
            if (specializationService.isNotUniqueSpecializationName(specializationDTO.getName())){
                errors.reject("name_unique", "NAME MUST BE UNIQUE");
            }
        }
    }
}

