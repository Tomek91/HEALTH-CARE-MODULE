package pl.com.app.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VisitDTO {
    private Long id;
    @NotNull(message = "Doctor is not correct")
    private DoctorDTO doctorDTO;
//    @NotNull(message = "Patient is not correct")
    private PatientDTO patientDTO;
    @NotNull(message = "Cost is not correct")
    @DecimalMin(value = "0", message = "Cost is not correct")
    private BigDecimal cost;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime time;
    private String description;
    private Boolean isAfterVisit;
    private Boolean isCanAddVisit;
}
