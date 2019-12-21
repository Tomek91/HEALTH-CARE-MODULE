package pl.com.app.service.annotations;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import pl.com.app.dto.DiseaseDTO;
import pl.com.app.service.DiseaseService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class DiseaseUniqueValidator implements ConstraintValidator<ADiseaseUnique, DiseaseDTO> {
    private final DiseaseService diseaseService;

    public void initialize(ADiseaseUnique constraint) {
    }

    public boolean isValid(DiseaseDTO diseaseDTO, ConstraintValidatorContext context) {
        if (diseaseDTO == null || StringUtils.isBlank(diseaseDTO.getName())) {
            return false;
        }

        if ((diseaseDTO.getId() == null || !diseaseService.getOneDisease(diseaseDTO.getId()).getName().equals(diseaseDTO.getName())) &&
                diseaseService.isNotUniqueDiseaseName(diseaseDTO.getName())) {
            return false;
        }
        return true;
    }
}
