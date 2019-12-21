package pl.com.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import pl.com.app.dto.PatientCardDTO;
import pl.com.app.exceptions.ExceptionCode;
import pl.com.app.exceptions.MyException;
import pl.com.app.model.*;
import pl.com.app.repository.*;
import pl.com.app.service.mappers.ModelMapper;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class PatientCardDiseaseService {
    private final PatientCardRepository patientCardRepository;
    private final DiseaseRepository diseaseRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final SpecializationRepository specializationRepository;
    private final ModelMapper modelMapper;

    public void addPatientCardDisease(PatientCardDTO patientCardDTO) {
        try {
            if (patientCardDTO == null) {
                throw new NullPointerException("PATIENT CARD IS NULL");
            }

            if (patientCardDTO.getDoctorDTO() == null) {
                throw new NullPointerException("DOCTOR IS NULL");
            }

            if (patientCardDTO.getDoctorDTO().getId() == null) {
                throw new NullPointerException("DOCTOR ID IS NULL");
            }

            if (patientCardDTO.getPatientDTO() == null) {
                throw new NullPointerException("PATIENT IS NULL");
            }

            if (patientCardDTO.getPatientDTO().getId() == null) {
                throw new NullPointerException("PATIENT ID IS NULL");
            }

            if (patientCardDTO.getDiseaseDTO() == null) {
                throw new NullPointerException("DISEASE IS NULL");
            }

            if (patientCardDTO.getDiseaseDTO().getSpecializationDTO() == null) {
                throw new NullPointerException("SPECIALIZATION IS NULL");
            }

            if (patientCardDTO.getDiseaseDTO().getSpecializationDTO().getId() == null) {
                throw new NullPointerException("SPECIALIZATION ID IS NULL");
            }

            Patient patient = patientRepository
                    .findById(patientCardDTO.getPatientDTO().getId())
                    .orElseThrow(NullPointerException::new);
            Doctor doctor = doctorRepository
                    .findById(patientCardDTO.getDoctorDTO().getId())
                    .orElseThrow(NullPointerException::new);
            Specialization specialization = specializationRepository
                    .findById(patientCardDTO.getDiseaseDTO().getSpecializationDTO().getId())
                    .orElseThrow(NullPointerException::new);

            Disease disease = modelMapper.fromDiseaseDTOToDisease(patientCardDTO.getDiseaseDTO());
            disease.setSpecialization(specialization);
            Disease newRecordDisease = diseaseRepository.save(disease);

            PatientCard patientCard = modelMapper.fromPatientCardDTOToPatientCard(patientCardDTO);
            patientCard.setDoctor(doctor);
            patientCard.setPatient(patient);
            patientCard.setDisease(newRecordDisease);
            patientCardRepository.save(patientCard);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public void modifyPatientCardDisease(PatientCardDTO patientCardDTO) {
        try {
            if (patientCardDTO == null) {
                throw new NullPointerException("PATIENT CARD IS NULL");
            }

            if (patientCardDTO.getDoctorDTO() == null) {
                throw new NullPointerException("DOCTOR IS NULL");
            }

            if (patientCardDTO.getDoctorDTO().getId() == null) {
                throw new NullPointerException("DOCTOR ID IS NULL");
            }

            if (patientCardDTO.getPatientDTO() == null) {
                throw new NullPointerException("PATIENT IS NULL");
            }

            if (patientCardDTO.getPatientDTO().getId() == null) {
                throw new NullPointerException("PATIENT ID IS NULL");
            }

            if (patientCardDTO.getDiseaseDTO() == null) {
                throw new NullPointerException("DISEASE IS NULL");
            }

            if (patientCardDTO.getDiseaseDTO().getId() == null) {
                throw new NullPointerException("DISEASE ID IS NULL");
            }

            if (patientCardDTO.getDiseaseDTO().getSpecializationDTO() == null) {
                throw new NullPointerException("SPECIALIZATION IS NULL");
            }

            if (patientCardDTO.getDiseaseDTO().getSpecializationDTO().getId() == null) {
                throw new NullPointerException("SPECIALIZATION ID IS NULL");
            }

            Patient patient = patientRepository
                    .findById(patientCardDTO.getPatientDTO().getId())
                    .orElseThrow(NullPointerException::new);
            Doctor doctor = doctorRepository
                    .findById(patientCardDTO.getDoctorDTO().getId())
                    .orElseThrow(NullPointerException::new);
            Specialization specialization = specializationRepository
                    .findById(patientCardDTO.getDiseaseDTO().getSpecializationDTO().getId())
                    .orElseThrow(NullPointerException::new);

            Disease disease = diseaseRepository.getOne(patientCardDTO.getDiseaseDTO().getId());
            disease.setSpecialization(specialization);
            disease.setImportance(patientCardDTO.getDiseaseDTO().getImportance() == null ? disease.getImportance() : patientCardDTO.getDiseaseDTO().getImportance() );
            disease.setName(patientCardDTO.getDiseaseDTO().getName() == null ? disease.getName()  : patientCardDTO.getDiseaseDTO().getName());
            Disease newRecordDisease = diseaseRepository.save(disease);

            PatientCard patientCard = patientCardRepository.getOne(patientCardDTO.getId());
            patientCard.setDoctor(doctor);
            patientCard.setPatient(patient);
            patientCard.setDisease(newRecordDisease);
            patientCard.setDescription(patientCardDTO.getDescription() == null ? patientCard.getDescription() : patientCardDTO.getDescription());
            patientCard.setDate(patientCardDTO.getDate() == null ? patientCard.getDate() : patientCardDTO.getDate());

            patientCardRepository.save(patientCard);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }
    public PatientCardDTO getOnePatientCardDisease(Long patientCardId) {
        try {
            if (patientCardId == null) {
                throw new NullPointerException("PATIENT CARD ID IS NULL");
            }

            return patientCardRepository
                    .findById(patientCardId)
                    .map(modelMapper::fromPatientCardToPatientCardDTO)
                    .orElseThrow(NullPointerException::new);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public List<PatientCardDTO> getAllPatientCardDiseases() {
        try {
            return patientCardRepository
                    .findAll()
                    .stream()
                    .map(modelMapper::fromPatientCardToPatientCardDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public void deletePatientCardDisease(Long patientCardId) {
        try {
            if (patientCardId == null) {
                throw new NullPointerException("PATIENT CARD ID IS NULL");
            }
            PatientCardDTO patientCardDTO = getOnePatientCardDisease(patientCardId);
            diseaseRepository.deleteById(patientCardDTO.getDiseaseDTO().getId());
            patientCardRepository.deleteById(patientCardId);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public List<PatientCardDTO> findAllPatientCardDiseasesForPatient(Long patientId) {
        try {
            if (patientId == null) {
                throw new NullPointerException("PATIENT ID IS NULL");
            }
            return patientCardRepository
                    .findAllByPatient_IdEqualsOrderByDateDesc(patientId)
                    .stream()
                    .map(modelMapper::fromPatientCardToPatientCardDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public List<PatientCardDTO> findAllPatientCardDiseasesByDoctor(Long doctorId) {
        try {
            if (doctorId == null) {
                throw new NullPointerException("PATIENT ID IS NULL");
            }
            return patientCardRepository
                    .findAllByDoctor_IdEquals(doctorId)
                    .stream()
                    .map(modelMapper::fromPatientCardToPatientCardDTO)
                    .sorted(Comparator.comparing(PatientCardDTO::getDate, Comparator.reverseOrder()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }
}
