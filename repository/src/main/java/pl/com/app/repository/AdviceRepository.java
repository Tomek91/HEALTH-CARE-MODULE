package pl.com.app.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import pl.com.app.model.Advice;

import java.util.List;


public interface AdviceRepository extends JpaRepository<Advice, Long> {

    long countAllByDoctor_IdEqualsAndAnswerIsNull(Long doctorId);
    List<Advice> findAllByDoctor_IdEqualsAndAnswerIsNull(Long doctorId);
    List<Advice> findAllByDoctorIsNullAndAnswerIsNull();
    List<Advice> findFirst5ByAnswerIsNotNullOrderByQuestionDateDesc();
    List<Advice> findAllByDoctorId_Equals(Long doctorId);
}
