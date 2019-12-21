package pl.com.app.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "patients")
public class Patient extends User{

    private String creditCardNumber;

    private String insuranceNumber;

    private EGender gender;

    private Double weight;

    private Integer height;

    @ManyToOne()
    @JoinColumn(name = "nationality_id", nullable = false)
    private Nationality nationality;

    @OneToMany(mappedBy = "patient", fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Visit> visits = new HashSet<>();

    @OneToMany(mappedBy = "patient", fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<PatientCard> patientCards = new HashSet<>();

    @OneToMany(mappedBy = "patient", fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Opinion> opinions = new HashSet<>();


    public Patient(Long id, String name, String surname, LocalDate birthday, String userName, String password, String email, Boolean active, LocalDate registrationDate, Role role, String creditCardNumber, String insuranceNumber, EGender gender, Double weight, Integer height, Nationality nationality, Set<Visit> visits, Set<PatientCard> patientCards, Set<Opinion> opinions) {
        super(id, name, surname, birthday, userName, password, email, active, registrationDate, role);
        this.creditCardNumber = creditCardNumber;
        this.insuranceNumber = insuranceNumber;
        this.gender = gender;
        this.weight = weight;
        this.height = height;
        this.nationality = nationality;
        this.visits = visits;
        this.patientCards = patientCards;
        this.opinions = opinions;
    }
}
