package pl.com.app.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.com.app.model.Doctor;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByNameAndSurname(String name, String surname);

    boolean existsByNameAndSurname(String name, String surname);

    boolean existsByWorkplace_IdEquals(Long workplaceId);

    boolean existsBySpecialization_IdEquals(Long specializationId);

    List<Doctor> findAllByNationality_IdEqualsAndSpecialization_IdEquals(Long nationalityId, Long specializationId);

    List<Doctor> findAllBySpecialization_IdEquals(Long specializationId);

    @Query("select d from Doctor d " +
            "join d.specialization s " +
            "join d.workplace w " +
            "join w.address a " +
            "where a.city = :city and " +
            "(d.name like %:nameOrSpecialization or s.name like %:nameOrSpecialization)")
    List<Doctor> findAllByCityAndNameOrSpecialization(@Param("city") String city, @Param("nameOrSpecialization") String nameOrSpecialization);
}
