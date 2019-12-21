package pl.com.app.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import pl.com.app.model.ENationalityName;
import pl.com.app.model.Nationality;

import java.util.Optional;

public interface NationalityRepository extends JpaRepository<Nationality, Long> {
    Optional<Nationality> findByName(ENationalityName name);
}
