package pl.com.app.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import pl.com.app.dto.DoctorDTO;
import pl.com.app.dto.OpinionDTO;
import pl.com.app.exceptions.ExceptionCode;
import pl.com.app.exceptions.MyException;
import pl.com.app.model.Doctor;
import pl.com.app.model.Opinion;
import pl.com.app.model.Patient;
import pl.com.app.repository.DoctorRepository;
import pl.com.app.repository.OpinionRepository;
import pl.com.app.repository.PatientRepository;
import pl.com.app.service.mappers.ModelMapper;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class OpinionService {
    private final OpinionRepository opinionRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;

    public void addOpinion(OpinionDTO opinionDTO){
        try {
            if (opinionDTO == null) {
                throw new NullPointerException("OPINION IS NULL");
            }

            if (opinionDTO.getDoctorDTO() == null) {
                throw new NullPointerException("DOCTOR IS NULL");
            }

            if (opinionDTO.getDoctorDTO().getId() == null) {
                throw new NullPointerException("DOCTOR ID IS NULL");
            }

            if (opinionDTO.getPatientDTO() == null) {
                throw new NullPointerException("PATIENT IS NULL");
            }

            if (opinionDTO.getPatientDTO().getId() == null) {
                throw new NullPointerException("PATIENT ID IS NULL");
            }

            Patient patient = patientRepository
                    .findById(opinionDTO.getPatientDTO().getId())
                    .orElseThrow(NullPointerException::new);
            Doctor doctor = doctorRepository
                    .findById(opinionDTO.getDoctorDTO().getId())
                    .orElseThrow(NullPointerException::new);

            Opinion opinion = modelMapper.fromOpinionDTOToOpinion(opinionDTO);
            opinion.setDoctor(doctor);
            opinion.setPatient(patient);
            opinionRepository.save(opinion);
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public void modifyOpinion(OpinionDTO opinionDTO) {
        try {
            if (opinionDTO == null) {
                throw new NullPointerException("OPINION IS NULL");
            }

            if (opinionDTO.getDoctorDTO() == null) {
                throw new NullPointerException("DOCTOR IS NULL");
            }

            if (opinionDTO.getDoctorDTO().getId() == null) {
                throw new NullPointerException("DOCTOR ID IS NULL");
            }

            if (opinionDTO.getPatientDTO() == null) {
                throw new NullPointerException("PATIENT IS NULL");
            }

            if (opinionDTO.getPatientDTO().getId() == null) {
                throw new NullPointerException("PATIENT ID IS NULL");
            }

            Doctor doctor = doctorRepository
                    .findById(opinionDTO.getDoctorDTO().getId())
                    .orElseThrow(NullPointerException::new);

            Patient patient = patientRepository
                    .findById(opinionDTO.getPatientDTO().getId())
                    .orElseThrow(NullPointerException::new);

            Opinion opinion = opinionRepository
                    .findById(opinionDTO.getId())
                    .orElseThrow(NullPointerException::new);

            opinion.setDoctor(doctor);
            opinion.setPatient(patient);

            opinion.setOpinionDate(opinionDTO.getOpinionDate() == null ? opinion.getOpinionDate() : opinionDTO.getOpinionDate());
            opinion.setQuality(opinionDTO.getQuality() == null ? opinion.getQuality() : opinionDTO.getQuality());
            opinion.setDescription(opinionDTO.getDescription() == null ? opinion.getDescription() : opinionDTO.getDescription());

            opinionRepository.save(opinion);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public OpinionDTO getOneOpinion(Long opinionId) {
        try {
            if (opinionId == null) {
                throw new NullPointerException("OPINION ID IS NULL");
            }

            return opinionRepository
                    .findById(opinionId)
                    .map(modelMapper::fromOpinionToOpinionDTO)
                    .orElseThrow(NullPointerException::new);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public List<OpinionDTO> getAllOpinions() {
        try {
            return opinionRepository
                    .findAllByOrderByOpinionDateDesc()
                    .stream()
                    .map(modelMapper::fromOpinionToOpinionDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public void deleteOpinion(Long opinionId) {
        try {
            if (opinionId == null) {
                throw new NullPointerException("OPINION ID IS NULL");
            }
            opinionRepository.deleteById(opinionId);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public Map<Long, List<OpinionDTO>> getAllOpinionsByDoctors() {
        try {
            return opinionRepository
                    .findAllByOrderByOpinionDateDesc()
                    .stream()
                    .map(modelMapper::fromOpinionToOpinionDTO)
                    .collect(Collectors.groupingBy(x -> x.getDoctorDTO().getId()));
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public Map<Long, List<OpinionDTO>> getAllOpinionsBySpecifiedDoctors(List<DoctorDTO> doctorsByCriteria) {
        try {
            if (doctorsByCriteria == null) {
                throw new NullPointerException("DOCTORS BY CRITERIA IS NULL");
            }
            return opinionRepository
                    .findAllByDoctor_IdInOrderByOpinionDateDesc(
                            doctorsByCriteria
                                .stream()
                                .map(DoctorDTO::getId)
                                .collect(Collectors.toList())
                    )
                    .stream()
                    .map(modelMapper::fromOpinionToOpinionDTO)
                    .collect(Collectors.groupingBy(x -> x.getDoctorDTO().getId()));
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public List<OpinionDTO> getNewestOpinions() {
        try {
            return opinionRepository
                    .findFirst5ByOrderByOpinionDateDesc()
                    .stream()
                    .map(modelMapper::fromOpinionToOpinionDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public List<OpinionDTO> getOpinionsByDoctor(Long id) {
        try {
            if (id == null) {
                throw new NullPointerException("DOCTORS ID IS NULL");
            }
            return opinionRepository
                    .findAllByDoctorId_Equals(id)
                    .stream()
                    .map(modelMapper::fromOpinionToOpinionDTO)
                    .sorted(Comparator.comparing(OpinionDTO::getOpinionDate, Comparator.reverseOrder()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public List<OpinionDTO> getOpinionsByPatient(Long patientId) {
        try {
            if (patientId == null) {
                throw new NullPointerException("PATIENT ID IS NULL");
            }
            return opinionRepository
                    .findAllByPatientId_Equals(patientId)
                    .stream()
                    .map(modelMapper::fromOpinionToOpinionDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }
}
