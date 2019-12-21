package pl.com.app.service.annotations;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import pl.com.app.service.SpecializationService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class SpecializationUniqueValidator implements ConstraintValidator<ASpecializationUnique, String> {
    private final SpecializationService specializationService;

    public void initialize(ASpecializationUnique constraint) {
    }

    public boolean isValid(String specializationName, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(specializationName) || specializationService.isNotUniqueSpecializationName(specializationName)) {
            return false;
        }
        return true;
    }
}
