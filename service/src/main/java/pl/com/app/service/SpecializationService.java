package pl.com.app.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import pl.com.app.dto.SpecializationDTO;
import pl.com.app.exceptions.ExceptionCode;
import pl.com.app.exceptions.MyException;
import pl.com.app.model.Specialization;
import pl.com.app.repository.DiseaseRepository;
import pl.com.app.repository.DoctorRepository;
import pl.com.app.repository.SpecializationRepository;
import pl.com.app.service.mappers.ModelMapper;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class SpecializationService {
    private final SpecializationRepository specializationRepository;
    private final DiseaseRepository diseaseRepository;
    private final DoctorRepository doctorRepository;
    private final ModelMapper modelMapper;

    public void addSpecialization(SpecializationDTO specializationDTO){
        try {
            if (specializationDTO == null) {
                throw new NullPointerException("SPECIALIZATION IS NULL");
            }

            specializationRepository.save(modelMapper.fromSpecializationDTOToSpecialization(specializationDTO));
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public void modifySpecialization(SpecializationDTO specializationDTO) {
        try {
            if (specializationDTO == null) {
                throw new NullPointerException("SPECIALIZATION IS NULL");
            }

            Specialization specialization = specializationRepository
                    .findById(specializationDTO.getId())
                    .orElseThrow(NullPointerException::new);

            specialization.setName(specializationDTO.getName() == null ? specialization.getName() : specializationDTO.getName());

            specializationRepository.save(specialization);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public SpecializationDTO getOneSpecialization(Long specializationId) {
        try {
            if (specializationId == null) {
                throw new NullPointerException("SPECIALIZATION ID IS NULL");
            }

            return specializationRepository
                    .findById(specializationId)
                    .map(modelMapper::fromSpecializationToSpecializationDTO)
                    .orElseThrow(NullPointerException::new);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public List<SpecializationDTO> getAllSpecializations() {
        try {
            return specializationRepository
                    .findAll()
                    .stream()
                    .map(modelMapper::fromSpecializationToSpecializationDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public void deleteSpecialization(Long specializationId) {
        try {
            if (specializationId == null) {
                throw new NullPointerException("SPECIALIZATION ID IS NULL");
            }
            if (doctorRepository.existsBySpecialization_IdEquals(specializationId)){
                throw new UnsupportedOperationException("CAN'T DELETE RECORD, IT USED IN DOCTOR");
            }
            if (diseaseRepository.existsBySpecialization_IdEquals(specializationId)){
                throw new UnsupportedOperationException("CAN'T DELETE RECORD, IT USED IN SPECIALIZATION");
            }
            specializationRepository.deleteById(specializationId);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public boolean isNotUniqueSpecializationName(String specializationName) {
        try {
            if (specializationName == null) {
                throw new NullPointerException("SPECIALIZATION NAME IS NULL");
            }
            return specializationRepository.existsByName(specializationName);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }
}
