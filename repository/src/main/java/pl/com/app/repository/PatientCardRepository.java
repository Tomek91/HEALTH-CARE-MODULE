package pl.com.app.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import pl.com.app.model.PatientCard;

import java.util.List;

public interface PatientCardRepository extends JpaRepository<PatientCard, Long> {
    void deleteAllByPatient_IdEquals(Long patientId);
    void deleteAllByDoctor_IdEquals(Long doctorId);

    List<PatientCard> findAllByPatient_IdEqualsOrderByDateDesc(Long patientId);

    List<PatientCard> findAllByDoctor_IdEquals(Long doctorId);
}
