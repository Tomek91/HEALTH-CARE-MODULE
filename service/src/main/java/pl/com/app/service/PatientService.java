package pl.com.app.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import pl.com.app.dto.PatientDTO;
import pl.com.app.exceptions.ExceptionCode;
import pl.com.app.exceptions.MyException;
import pl.com.app.model.Nationality;
import pl.com.app.model.Patient;
import pl.com.app.repository.*;
import pl.com.app.service.mappers.ModelMapper;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientCardRepository patientCardRepository;
    private final VisitRepository visitRepository;
    private final OpinionRepository opinionRepository;
    private final DiseaseRepository diseaseRepository;
    private final NationalityRepository nationalityRepository;
    private final ModelMapper modelMapper;

    public void addPatient(PatientDTO patientDTO){
        try {
            if (patientDTO == null) {
                throw new NullPointerException("PATIENT IS NULL");
            }

            if (patientDTO.getNationalityDTO() == null) {
                throw new NullPointerException("NATIONALITY IS NULL");
            }

            if (patientDTO.getNationalityDTO().getId() == null) {
                throw new NullPointerException("NATIONALITY ID IS NULL");
            }

            Nationality nationality = nationalityRepository
                    .findById(patientDTO.getNationalityDTO().getId())
                    .orElseThrow(NullPointerException::new);

            Patient patient = modelMapper.fromPatientDTOToPatient(patientDTO);
            patient.setNationality(nationality);
            patientRepository.save(patient);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public void modifyPatient(PatientDTO patientDTO) {
        try {
            if (patientDTO == null) {
                throw new NullPointerException("PATIENT IS NULL");
            }

            if (patientDTO.getNationalityDTO() == null) {
                throw new NullPointerException("NATIONALITY IS NULL");
            }

            if (patientDTO.getNationalityDTO().getId() == null) {
                throw new NullPointerException("NATIONALITY ID IS NULL");
            }

            Nationality nationality = nationalityRepository
                    .findById(patientDTO.getNationalityDTO().getId())
                    .orElseThrow(NullPointerException::new);

            Patient patient = patientRepository
                    .findById(patientDTO.getId())
                    .orElseThrow(NullPointerException::new);

            patient.setNationality(nationality);

            patient.setCreditCardNumber(patientDTO.getCreditCardNumber() == null ? patient.getCreditCardNumber() : patientDTO.getCreditCardNumber());
            patient.setGender(patientDTO.getGender() == null ? patient.getGender() : patientDTO.getGender());
            patient.setHeight(patientDTO.getHeight() == null ? patient.getHeight() : patientDTO.getHeight());
            patient.setInsuranceNumber(patientDTO.getInsuranceNumber() == null ? patient.getInsuranceNumber() : patientDTO.getInsuranceNumber());
            patient.setWeight(patientDTO.getWeight() == null ? patient.getWeight() : patientDTO.getWeight());
            patient.setActive(patientDTO.getActive() == null ? patient.getActive() : patientDTO.getActive());
            patient.setBirthday(patientDTO.getBirthday() == null ? patient.getBirthday() : patientDTO.getBirthday());
            patient.setEmail(patientDTO.getEmail() == null ? patient.getEmail() : patientDTO.getEmail());
            patient.setName(patientDTO.getName() == null ? patient.getName() : patientDTO.getName());
            patient.setSurname(patientDTO.getSurname() == null ? patient.getSurname() : patientDTO.getSurname());
            patient.setUserName(patientDTO.getUserName() == null ? patient.getUserName() : patientDTO.getUserName());
            patient.setPassword(patientDTO.getPassword() == null ? patient.getPassword() : patientDTO.getPassword());
            patient.setRegistrationDate(patientDTO.getRegistrationDate() == null ? patient.getRegistrationDate() : patientDTO.getRegistrationDate());

            patientRepository.save(patient);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public PatientDTO getOnePatient(Long patientId) {
        try {
            if (patientId == null) {
                throw new NullPointerException("PATIENT ID IS NULL");
            }

            return patientRepository
                    .findById(patientId)
                    .map(modelMapper::fromPatientToPatientDTO)
                    .orElseThrow(NullPointerException::new);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public List<PatientDTO> getAllPatients() {
        try {
            return patientRepository
                    .findAll()
                    .stream()
                    .map(modelMapper::fromPatientToPatientDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public void deletePatient(Long patientId) {
        try {
            if (patientId == null) {
                throw new NullPointerException("PATIENT ID IS NULL");
            }
            opinionRepository.deleteAllByPatient_IdEquals(patientId);
            visitRepository.deleteAllByPatient_IdEquals(patientId);
            patientCardRepository.findAllByDoctor_IdEquals(patientId)
                    .forEach(x -> diseaseRepository.deleteById(x.getDisease().getId()));
            patientCardRepository.deleteAllByPatient_IdEquals(patientId);

            patientRepository.deleteById(patientId);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public boolean isNotUniqueNameAndSurname(String name, String surname) {
        try {
            if (name == null || surname == null) {
                throw new NullPointerException("ARGS ARE NULL");
            }
            return patientRepository.existsByNameAndSurname(name, surname);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }
}
