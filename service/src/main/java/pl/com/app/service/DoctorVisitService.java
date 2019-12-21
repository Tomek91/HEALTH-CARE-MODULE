package pl.com.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import pl.com.app.dto.DiseaseDTO;
import pl.com.app.dto.PatientCardDTO;
import pl.com.app.dto.VisitDTO;
import pl.com.app.exceptions.ExceptionCode;
import pl.com.app.exceptions.MyException;
import pl.com.app.repository.VisitRepository;
import pl.com.app.service.mappers.ModelMapper;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class DoctorVisitService {
    private final VisitRepository visitRepository;
    private final ModelMapper modelMapper;

    public List<VisitDTO> getAllVisitsForDoctor(Long doctorId) {
        try {
            if (doctorId == null) {
                throw new NullPointerException("DOCTOR ID IS NULL");
            }
            return visitRepository
                    .findAllByDoctorAndNotActiveVisit(doctorId)
                    .stream()
                    .map(modelMapper::fromVisitToVisitDTO)
                    .peek(x ->
                            x.setIsCanAddVisit(x.getDate().isEqual(LocalDate.now()) &&
                                    x.getTime().isBefore(LocalTime.now().plusMinutes(10L)) &&
                                    x.getTime().isAfter(LocalTime.now().minusMinutes(10L)))
                    )
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public PatientCardDTO getPatientCardByVisitId(Long visitId) {
        try {
            if (visitId == null) {
                throw new NullPointerException("VISIT ID IS NULL");
            }
            VisitDTO visitDTO = visitRepository
                    .findById(visitId)
                    .map(modelMapper::fromVisitToVisitDTO)
                    .orElseThrow(NullPointerException::new);

            return PatientCardDTO
                    .builder()
                    .doctorDTO(visitDTO.getDoctorDTO())
                    .patientDTO(visitDTO.getPatientDTO())
                    .diseaseDTO(
                            DiseaseDTO
                                    .builder()
                                    .specializationDTO(
                                            visitDTO.getDoctorDTO().getSpecializationDTO()
                                    )
                                    .build()
                    )
                    .date(LocalDate.now())
                    .build();

        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }
}
