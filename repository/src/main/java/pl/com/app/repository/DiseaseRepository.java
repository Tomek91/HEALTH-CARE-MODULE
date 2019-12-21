package pl.com.app.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import pl.com.app.model.Disease;

import java.util.Optional;

public interface DiseaseRepository extends JpaRepository<Disease, Long> {
    Optional<Disease> findByName(String name);

    boolean existsByName(String diseaseName);

    boolean existsBySpecialization_IdEquals(Long specializationId);
}
