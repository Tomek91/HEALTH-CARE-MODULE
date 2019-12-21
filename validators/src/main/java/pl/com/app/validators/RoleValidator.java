package pl.com.app.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import pl.com.app.dto.RoleDTO;
import pl.com.app.exceptions.ExceptionCode;
import pl.com.app.exceptions.MyException;

@Component
@RequiredArgsConstructor
public class RoleValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(RoleDTO.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        if (o == null) {
            throw new MyException(ExceptionCode.VALIDATION, "OBJECT IS NULL");
        }

        RoleDTO roleDTO = (RoleDTO) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "id", "id", "You have to choose ROLE.");

    }
}
