package pl.com.app.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class DoctorDTO extends UserDTO {
    private String experience;
    private MultipartFile file;
    private String photo;
    private String description;
    private BigDecimal cost;
    private SpecializationDTO specializationDTO;
    private WorkplaceDTO workplaceDTO;
    private NationalityDTO nationalityDTO;


    public DoctorDTO(Long id, String name, String surname, LocalDate birthday, String userName, String password, String passwordConfirmation, String email, Boolean active, LocalDate registrationDate, RoleDTO roleDTO, String experience, String photo, String description, BigDecimal cost, SpecializationDTO specializationDTO, WorkplaceDTO workplaceDTO, NationalityDTO nationalityDTO) {
        super(id, name, surname, birthday, userName, password, passwordConfirmation, email, active, registrationDate, roleDTO);
        this.experience = experience;
        this.photo = photo;
        this.description = description;
        this.cost = cost;
        this.specializationDTO = specializationDTO;
        this.workplaceDTO = workplaceDTO;
        this.nationalityDTO = nationalityDTO;
    }
}
