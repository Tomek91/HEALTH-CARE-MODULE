package pl.com.app.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OpinionDTO {
    private Long id;
    private DoctorDTO doctorDTO;
    private PatientDTO patientDTO;
    private String description;
    @Range(min = 1, max = 5, message = "Quality is not correct")
    private Integer quality;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate opinionDate;
}
