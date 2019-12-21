package pl.com.app.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.com.app.model.EImportance;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiseaseDTO {
    private Long id;
    @NotBlank(message = "Name can not be empty.")
    private String name;
    private EImportance importance;
    private SpecializationDTO specializationDTO;
}
