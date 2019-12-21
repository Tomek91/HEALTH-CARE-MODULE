package pl.com.app.model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;

    @Column(unique = true)
    private String street;

    private Integer number;

    private String postalCode;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "address")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Workplace> workplaces = new HashSet<>();
}
