package pl.com.app.validators;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pl.com.app.dto.WorkplaceDTO;
import pl.com.app.exceptions.ExceptionCode;
import pl.com.app.exceptions.MyException;
import pl.com.app.service.WorkplaceService;

@Component
@RequiredArgsConstructor
public class WorkspaceValidator implements Validator {
    private final WorkplaceService workplaceService;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(WorkplaceDTO.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        if (o == null) {
            throw new MyException(ExceptionCode.VALIDATION, "OBJECT IS NULL");
        }

        WorkplaceDTO workplaceDTO = (WorkplaceDTO) o;

        if (StringUtils.isBlank(workplaceDTO.getName()) || !workplaceDTO.getName().matches("[A-Z ]+")) {
            errors.rejectValue("name", "WorkPlace is not correct");
        }
        
        if (!errors.hasErrors()) {
            if (workplaceDTO.getId() == null || !workplaceService.getOneWorkplace(workplaceDTO.getId()).getName().equals(workplaceDTO.getName())){
                if (workplaceService.isNotUniqueWorkplaceName(workplaceDTO.getName())){
                    errors.reject("name_unique", "NAME MUST BE UNIQUE");
                }
            }
        }
    }
}

