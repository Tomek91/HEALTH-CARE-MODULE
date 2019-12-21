package pl.com.app.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VisitCriteriaDTO {
    private SpecializationDTO specializationDTO;
    private NationalityDTO nationalityDTO;
}
