package pl.com.app.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import pl.com.app.model.Workplace;

import java.util.Optional;

public interface WorkplaceRepository extends JpaRepository<Workplace, Long> {
    Optional<Workplace> findByName(String name);

    boolean existsByName(String workplaceName);

    boolean existsByAddress_IdEquals(Long addressId);
}
