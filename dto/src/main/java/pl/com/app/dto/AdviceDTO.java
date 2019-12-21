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
public class AdviceDTO {
    private Long id;
    private DoctorDTO doctorDTO;
    private SpecializationDTO specializationDTO;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate questionDate;
    @NotBlank(message = "Email can not be empty.")
    private String email;
    @NotBlank(message = "Question can not be empty.")
    private String question;
    private String answer;
}
