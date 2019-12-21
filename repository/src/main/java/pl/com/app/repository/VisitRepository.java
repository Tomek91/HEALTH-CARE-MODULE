package pl.com.app.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.com.app.model.Visit;

import java.util.List;

public interface VisitRepository extends JpaRepository<Visit, Long> {
    void deleteAllByPatient_IdEquals(Long patientId);
    void deleteAllByDoctor_IdEquals(Long doctorId);

    List<Visit> findAllByDoctorId_Equals(Long doctorId);

    @Modifying
    @Query("update Visit v set v.isAfterVisit = true where v.id = ?1")
    void setEndedVisit(Long visitId);

    @Query("select v from Visit v join v.doctor d where d.id = :doctorId and v.isAfterVisit = false and v.dateTime >= CURRENT_TIMESTAMP order by v.dateTime")
    List<Visit> findAllByDoctorAndNotActiveVisit(@Param("doctorId") Long doctorId);

}
