package pl.com.app.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientCardDTO {
    private Long id;
    private DoctorDTO doctorDTO;
    private PatientDTO patientDTO;
   // @ADiseaseUnique()
    private DiseaseDTO diseaseDTO;
    @NotBlank(message = "Description can not be empty.")
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
}
