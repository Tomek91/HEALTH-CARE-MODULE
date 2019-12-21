package pl.com.app.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import pl.com.app.model.Opinion;

import java.util.List;

public interface OpinionRepository extends JpaRepository<Opinion, Long> {
    void deleteAllByPatient_IdEquals(Long patientId);
    void deleteAllByDoctor_IdEquals(Long doctorId);

    List<Opinion> findAllByOrderByOpinionDateDesc();
    List<Opinion> findFirst5ByOrderByOpinionDateDesc();
    List<Opinion> findAllByDoctor_IdInOrderByOpinionDateDesc(List<Long> doctorsIdByCriteria);
    List<Opinion> findAllByDoctor_IdEqualsOrderByQualityDesc(Long doctorsId);
    List<Opinion> findAllByDoctorId_Equals(Long doctorId);
    List<Opinion> findAllByPatientId_Equals(Long doctorId);

}
