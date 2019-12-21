package pl.com.app.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import pl.com.app.dto.DoctorDTO;
import pl.com.app.dto.ScheduleHoursDTO;
import pl.com.app.dto.VisitDTO;
import pl.com.app.exceptions.ExceptionCode;
import pl.com.app.exceptions.MyException;
import pl.com.app.model.Doctor;
import pl.com.app.model.Patient;
import pl.com.app.model.Visit;
import pl.com.app.repository.DoctorRepository;
import pl.com.app.repository.PatientRepository;
import pl.com.app.repository.VisitRepository;
import pl.com.app.service.mappers.ModelMapper;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class VisitService {
    private final VisitRepository visitRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final ModelMapper modelMapper;

    public void addVisit(VisitDTO visitDTO){
        try {
            if (visitDTO == null) {
                throw new NullPointerException("VISIT IS NULL");
            }

            if (visitDTO.getDoctorDTO() == null) {
                throw new NullPointerException("DOCTOR IS NULL");
            }

            if (visitDTO.getDoctorDTO().getId() == null) {
                throw new NullPointerException("DOCTOR ID IS NULL");
            }

            if (visitDTO.getPatientDTO() == null) {
                throw new NullPointerException("PATIENT IS NULL");
            }

            if (visitDTO.getPatientDTO().getId() == null) {
                throw new NullPointerException("PATIENT ID IS NULL");
            }

            Patient patient = patientRepository
                    .findById(visitDTO.getPatientDTO().getId())
                    .orElseThrow(NullPointerException::new);
            Doctor doctor = doctorRepository
                    .findById(visitDTO.getDoctorDTO().getId())
                    .orElseThrow(NullPointerException::new);
            Visit visit = modelMapper.fromVisitDTOToVisit(visitDTO);
            visit.setDoctor(doctor);
            visit.setPatient(patient);
            visit.setIsAfterVisit(false);
            visitRepository.save(visit);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public void modifyVisit(VisitDTO visitDTO) {
        try {
            if (visitDTO == null) {
                throw new NullPointerException("VISIT IS NULL");
            }

            if (visitDTO.getDoctorDTO() == null) {
                throw new NullPointerException("DOCTOR IS NULL");
            }

            if (visitDTO.getDoctorDTO().getId() == null) {
                throw new NullPointerException("DOCTOR ID IS NULL");
            }

            if (visitDTO.getPatientDTO() == null) {
                throw new NullPointerException("PATIENT IS NULL");
            }

            if (visitDTO.getPatientDTO().getId() == null) {
                throw new NullPointerException("PATIENT ID IS NULL");
            }

            Patient patient = patientRepository
                    .findById(visitDTO.getPatientDTO().getId())
                    .orElseThrow(NullPointerException::new);
            Doctor doctor = doctorRepository
                    .findById(visitDTO.getDoctorDTO().getId())
                    .orElseThrow(NullPointerException::new);

            Visit visit = visitRepository.getOne(visitDTO.getId());
            visit.setPatient(patient);
            visit.setDoctor(doctor);
            visit.setCost(visitDTO.getCost() == null ? visit.getCost() : visitDTO.getCost());
            visit.setDateTime(visitDTO.getDate() == null || visitDTO.getTime() == null ? visit.getDateTime() : LocalDateTime.of(visitDTO.getDate(), visitDTO.getTime()));
            visit.setDescription(visitDTO.getDescription() == null ? visit.getDescription() : visitDTO.getDescription());

            visitRepository.save(visit);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public VisitDTO getOneVisit(Long visitId) {
        try {
            if (visitId == null) {
                throw new NullPointerException("VISIT ID IS NULL");
            }

            return visitRepository
                    .findById(visitId)
                    .map(modelMapper::fromVisitToVisitDTO)
                    .orElseThrow(NullPointerException::new);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public List<VisitDTO> getAllVisits() {
        try {
            return visitRepository
                    .findAll()
                    .stream()
                    .map(modelMapper::fromVisitToVisitDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public void deleteVisit(Long visitId) {
        try {
            if (visitId == null) {
                throw new NullPointerException("VISIT ID IS NULL");
            }
            visitRepository.deleteById(visitId);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public void setEndedVisit(Long visitId) {
        try {
            if (visitId == null) {
                throw new NullPointerException("VISIT ID IS NULL");
            }
            visitRepository.setEndedVisit(visitId);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public Map<Long, Map<LocalDate, List<ScheduleHoursDTO>>> getAllSchedulesByDoctors() {
        try {
            return doctorRepository
                    .findAll()
                    .stream()
                    .map(modelMapper::fromDoctorToDoctorDTO)
                    .collect(Collectors.toMap(
                            DoctorDTO::getId,
                            x -> createScheduleHoursFromDateTimeNow(x.getId())
                    ));
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    private Map<LocalDate, List<ScheduleHoursDTO>> createScheduleHoursFromDateTimeNow(Long doctorId) {
        try {
            if (doctorId == null) {
                throw new NullPointerException("DOCTOR ID IS NULL");
            }
            List<LocalDateTime> doctorVisitsOccupied = visitRepository.findAllByDoctorAndNotActiveVisit(doctorId)
                    .stream()
                    .map(Visit::getDateTime)
                    .collect(Collectors.toList());

            return generateScheduleDays()
                    .stream()
                    .collect(Collectors.toMap(
                            Function.identity(),
                            d -> generateScheduleHours()
                                    .stream()
                                    .peek(s ->
                                            s.setActive(
                                                    !(d.equals(LocalDate.now()) && s.getHours().isBefore(LocalTime.now())) &&
                                                    !(doctorVisitsOccupied.contains(LocalDateTime.of(d, s.getHours())))
                                            )
                                    )
                                    .collect(Collectors.toList())
                    ))
                    .entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByKey())
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (v1, v2) -> v1,
                            LinkedHashMap::new)
                    );
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    private List<ScheduleHoursDTO> generateScheduleHours() {
        List<ScheduleHoursDTO> scheduleHours = new ArrayList<>();
        LocalTime startHour = LocalTime.of(8, 0);
        while (startHour.compareTo(LocalTime.of(16, 0)) <= 0){
            scheduleHours.add(ScheduleHoursDTO.builder().hours(startHour).isActive(true).build());
            startHour = startHour.plusMinutes(30L);
        }
        return scheduleHours;
    }

    private List<LocalDate> generateScheduleDays() {
        final int scheduleForNumberOfDays = 2;
        List<LocalDate> scheduleDays = new ArrayList<>();
        for (int i = 0; i < scheduleForNumberOfDays; i++) {
            scheduleDays.add(LocalDate.now().plusDays(i));
        }
        return scheduleDays;
    }

    public Map<Long, Map<LocalDate, List<ScheduleHoursDTO>>> getAllSchedulesBySpecifiedDoctors(List<DoctorDTO> doctorsByCriteria) {
        try {
            if (doctorsByCriteria == null) {
                throw new NullPointerException("DOCTORS BY CRITERIA IS NULL");
            }
            return doctorRepository
                    .findAllById(
                            doctorsByCriteria
                                    .stream()
                                    .map(DoctorDTO::getId)
                                    .collect(Collectors.toList())
                    )
                    .stream()
                    .map(modelMapper::fromDoctorToDoctorDTO)
                    .collect(Collectors.toMap(
                            DoctorDTO::getId,
                            x -> createScheduleHoursFromDateTimeNow(x.getId())
                    ));
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public List<VisitDTO> getVisitsByDoctor(Long id) {
        try {
            if (id == null) {
                throw new NullPointerException("DOCTORS ID IS NULL");
            }
            return visitRepository
                    .findAllByDoctorId_Equals(id)
                    .stream()
                    .map(modelMapper::fromVisitToVisitDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }
}
