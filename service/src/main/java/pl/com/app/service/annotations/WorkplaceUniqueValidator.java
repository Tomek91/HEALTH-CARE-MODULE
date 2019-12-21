package pl.com.app.service.annotations;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import pl.com.app.service.WorkplaceService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class WorkplaceUniqueValidator implements ConstraintValidator<AWorkplaceUnique, String> {
    private final WorkplaceService workplaceService;

    public void initialize(AWorkplaceUnique constraint) {
    }

    public boolean isValid(String workplaceName, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(workplaceName) || workplaceService.isNotUniqueWorkplaceName(workplaceName)) {
            return false;
        }
        return true;
    }
}
