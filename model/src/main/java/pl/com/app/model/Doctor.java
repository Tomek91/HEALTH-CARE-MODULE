package pl.com.app.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "doctors")
public class Doctor extends User {

    private String experience;

    private String photo;

    private String description;

    private BigDecimal cost;

    @ManyToOne()
    @JoinColumn(name = "specialization_id", nullable = false)
    private Specialization specialization;

    @ManyToOne()
    @JoinColumn(name = "workplace_id", nullable = false)
    private Workplace workplace;

    @ManyToOne()
    @JoinColumn(name = "nationality_id", nullable = false)
    private Nationality nationality;

    @OneToMany(mappedBy = "doctor", fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Opinion> opinions = new HashSet<>();

    @OneToMany(mappedBy = "doctor", fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Visit> visits = new HashSet<>();

    @OneToMany(mappedBy = "doctor", fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<PatientCard> patientCards = new HashSet<>();

    @OneToMany(mappedBy = "doctor", fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Advice> advices = new HashSet<>();


    public Doctor(Long id, String name, String surname, LocalDate birthday, String userName, String password, String email, Boolean active, LocalDate registrationDate, Role role, String experience, String photo, String description, BigDecimal cost, Specialization specialization, Workplace workplace, Nationality nationality, Set<Opinion> opinions, Set<Visit> visits, Set<PatientCard> patientCards, Set<Advice> advices) {
        super(id, name, surname, birthday, userName, password, email, active, registrationDate, role);
        this.experience = experience;
        this.photo = photo;
        this.description = description;
        this.cost = cost;
        this.specialization = specialization;
        this.workplace = workplace;
        this.nationality = nationality;
        this.opinions = opinions;
        this.visits = visits;
        this.patientCards = patientCards;
        this.advices = advices;
    }
}
