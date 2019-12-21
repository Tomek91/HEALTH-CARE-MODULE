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
public class AdviceAnswerDTO {
    private Long id;
    private DoctorDTO doctorDTO;
    private SpecializationDTO specializationDTO;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate questionDate;
    private String email;
    private String question;
    @NotBlank(message = "Answer can not be empty.")
    private String answer;
}
