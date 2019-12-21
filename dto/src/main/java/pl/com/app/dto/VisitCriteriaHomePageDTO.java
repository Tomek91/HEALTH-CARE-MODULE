package pl.com.app.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VisitCriteriaHomePageDTO {
    @NotBlank(message = "Name or specialization is not correct.")
    private String nameOrSpecialization;
    @NotBlank(message = "City is not correct.")
    private String city;
}
