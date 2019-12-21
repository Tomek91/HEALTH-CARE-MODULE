package pl.com.app.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import pl.com.app.dto.DoctorDTO;
import pl.com.app.exceptions.ExceptionCode;
import pl.com.app.exceptions.MyException;
import pl.com.app.model.Doctor;
import pl.com.app.model.Nationality;
import pl.com.app.model.Specialization;
import pl.com.app.model.Workplace;
import pl.com.app.repository.*;
import pl.com.app.service.mappers.ModelMapper;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final NationalityRepository nationalityRepository;
    private final SpecializationRepository specializationRepository;
    private final WorkplaceRepository workplaceRepository;
    private final PatientCardRepository patientCardRepository;
    private final DiseaseRepository diseaseRepository;
    private final VisitRepository visitRepository;
    private final OpinionRepository opinionRepository;
    private final FileService fileService;
    private final ModelMapper modelMapper;

    public void addDoctor(DoctorDTO doctorDTO) {
        try {
            if (doctorDTO == null) {
                throw new NullPointerException("DOCTOR IS NULL");
            }

            if (doctorDTO.getNationalityDTO() == null) {
                throw new NullPointerException("NATIONALITY IS NULL");
            }

            if (doctorDTO.getNationalityDTO().getId() == null) {
                throw new NullPointerException("NATIONALITY ID IS NULL");
            }

            if (doctorDTO.getWorkplaceDTO() == null) {
                throw new NullPointerException("WORKPLACE IS NULL");
            }

            if (doctorDTO.getWorkplaceDTO().getId() == null) {
                throw new NullPointerException("WORKPLACE ID IS NULL");
            }

            if (doctorDTO.getSpecializationDTO() == null) {
                throw new NullPointerException("SPECIALIZATION IS NULL");
            }

            if (doctorDTO.getSpecializationDTO().getId() == null) {
                throw new NullPointerException("SPECIALIZATION ID IS NULL");
            }

            Nationality nationality = nationalityRepository
                    .findById(doctorDTO.getNationalityDTO().getId())
                    .orElseThrow(NullPointerException::new);
            Specialization specialization = specializationRepository
                    .findById(doctorDTO.getSpecializationDTO().getId())
                    .orElseThrow(NullPointerException::new);
            Workplace workplace = workplaceRepository
                    .findById(doctorDTO.getWorkplaceDTO().getId())
                    .orElseThrow(NullPointerException::new);

            Doctor doctor = modelMapper.fromDoctorDTOToDoctor(doctorDTO);
            String filename = fileService.addFile(doctorDTO.getFile());
            doctor.setPhoto(filename);
            doctor.setNationality(nationality);
            doctor.setWorkplace(workplace);
            doctor.setSpecialization(specialization);
            doctorRepository.save(doctor);
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public void modifyDoctor(DoctorDTO doctorDTO) {
        try {
            if (doctorDTO == null) {
                throw new NullPointerException("DOCTOR IS NULL");
            }

            if (doctorDTO.getNationalityDTO() == null) {
                throw new NullPointerException("NATIONALITY IS NULL");
            }

            if (doctorDTO.getNationalityDTO().getId() == null) {
                throw new NullPointerException("NATIONALITY ID IS NULL");
            }

            if (doctorDTO.getWorkplaceDTO() == null) {
                throw new NullPointerException("WORKPLACE IS NULL");
            }

            if (doctorDTO.getWorkplaceDTO().getId() == null) {
                throw new NullPointerException("WORKPLACE ID IS NULL");
            }

            if (doctorDTO.getSpecializationDTO() == null) {
                throw new NullPointerException("SPECIALIZATION IS NULL");
            }

            if (doctorDTO.getSpecializationDTO().getId() == null) {
                throw new NullPointerException("SPECIALIZATION ID IS NULL");
            }

            Nationality nationality = nationalityRepository
                    .findById(doctorDTO.getNationalityDTO().getId())
                    .orElseThrow(NullPointerException::new);

            Workplace workplace = workplaceRepository
                    .findById(doctorDTO.getWorkplaceDTO().getId())
                    .orElseThrow(NullPointerException::new);

            Specialization specialization = specializationRepository
                    .findById(doctorDTO.getSpecializationDTO().getId())
                    .orElseThrow(NullPointerException::new);

            Doctor doctor = doctorRepository
                    .findById(doctorDTO.getId())
                    .orElseThrow(NullPointerException::new);

            doctor.setNationality(nationality);
            doctor.setSpecialization(specialization);
            doctor.setWorkplace(workplace);
            String filename = fileService.updateFile(doctorDTO.getFile(), doctorDTO.getPhoto());

            doctor.setExperience(doctorDTO.getExperience() == null ? doctor.getExperience() : doctorDTO.getExperience());
            doctor.setDescription(doctorDTO.getDescription() == null ? doctor.getDescription() : doctorDTO.getDescription());
            doctor.setCost(doctorDTO.getCost() == null ? doctor.getCost() : doctorDTO.getCost());
            doctor.setActive(doctorDTO.getActive() == null ? doctor.getActive() : doctorDTO.getActive());
            doctor.setBirthday(doctorDTO.getBirthday() == null ? doctor.getBirthday() : doctorDTO.getBirthday());
            doctor.setEmail(doctorDTO.getEmail() == null ? doctor.getEmail() : doctorDTO.getEmail());
            doctor.setName(doctorDTO.getName() == null ? doctor.getName() : doctorDTO.getName());
            doctor.setSurname(doctorDTO.getSurname() == null ? doctor.getSurname() : doctorDTO.getSurname());
            doctor.setUserName(doctorDTO.getUserName() == null ? doctor.getUserName() : doctorDTO.getUserName());
            doctor.setPassword(doctorDTO.getPassword() == null ? doctor.getPassword() : doctorDTO.getPassword());
            doctor.setRegistrationDate(doctorDTO.getRegistrationDate() == null ? doctor.getRegistrationDate() : doctorDTO.getRegistrationDate());

            doctorRepository.save(doctor);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public DoctorDTO getOneDoctor(Long doctorId) {
        try {
            if (doctorId == null) {
                throw new NullPointerException("DOCTOR ID IS NULL");
            }

            return doctorRepository
                    .findById(doctorId)
                    .map(modelMapper::fromDoctorToDoctorDTO)
                    .orElseThrow(NullPointerException::new);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public List<DoctorDTO> getAllDoctors() {
        try {
            return doctorRepository
                    .findAll()
                    .stream()
                    .map(modelMapper::fromDoctorToDoctorDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public void deleteDoctor(Long doctorId) {
        try {
            if (doctorId == null) {
                throw new NullPointerException("DOCTOR ID IS NULL");
            }
            opinionRepository.deleteAllByDoctor_IdEquals(doctorId);
            visitRepository.deleteAllByDoctor_IdEquals(doctorId);
            patientCardRepository.findAllByDoctor_IdEquals(doctorId)
                    .forEach(x -> diseaseRepository.deleteById(x.getDisease().getId()));
            patientCardRepository.deleteAllByDoctor_IdEquals(doctorId);

            DoctorDTO doctorDTO = getOneDoctor(doctorId);
            doctorRepository.deleteById(doctorId);
            fileService.deleteFile(doctorDTO.getPhoto());
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
            return doctorRepository.existsByNameAndSurname(name, surname);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }
}
