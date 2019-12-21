package pl.com.app.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import pl.com.app.model.EGender;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class PatientDTO extends UserDTO {
    private String creditCardNumber;
    private String insuranceNumber;
    private EGender gender;
    private Double weight;
    private Integer height;
    private NationalityDTO nationalityDTO;


    public PatientDTO(Long id, String name, String surname, LocalDate birthday, String userName, String password, String passwordConfirmation, String email, Boolean active, LocalDate registrationDate, RoleDTO roleDTO, String creditCardNumber, String insuranceNumber, EGender gender, Double weight, Integer height, NationalityDTO nationalityDTO) {
        super(id, name, surname, birthday, userName, password, passwordConfirmation, email, active, registrationDate, roleDTO);
        this.creditCardNumber = creditCardNumber;
        this.insuranceNumber = insuranceNumber;
        this.gender = gender;
        this.weight = weight;
        this.height = height;
        this.nationalityDTO = nationalityDTO;
    }
}
