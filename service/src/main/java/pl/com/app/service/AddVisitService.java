package pl.com.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import pl.com.app.dto.DoctorDTO;
import pl.com.app.dto.VisitCriteriaDTO;
import pl.com.app.dto.VisitCriteriaHomePageDTO;
import pl.com.app.exceptions.ExceptionCode;
import pl.com.app.exceptions.MyException;
import pl.com.app.repository.DoctorRepository;
import pl.com.app.service.mappers.ModelMapper;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AddVisitService {
    private final DoctorRepository doctorRepository;
    private final ModelMapper modelMapper;

    public List<DoctorDTO> findAllDoctorsByVisitCriteria(VisitCriteriaDTO visitCriteriaDTO) {
        try {
            if (visitCriteriaDTO == null) {
                throw new NullPointerException("VISIT CRITERIA IS NULL");
            }
            if (visitCriteriaDTO.getNationalityDTO() == null) {
                throw new NullPointerException("NATIONALITY IS NULL");
            }
            if (visitCriteriaDTO.getNationalityDTO().getId() == null) {
                throw new NullPointerException("NATIONALITY ID IS NULL");
            }
            if (visitCriteriaDTO.getSpecializationDTO() == null) {
                throw new NullPointerException("SPECIALIZATION IS NULL");
            }
            if (visitCriteriaDTO.getSpecializationDTO().getId() == null) {
                throw new NullPointerException("SPECIALIZATION ID IS NULL");
            }
            return doctorRepository.findAllByNationality_IdEqualsAndSpecialization_IdEquals(visitCriteriaDTO.getNationalityDTO().getId(), visitCriteriaDTO.getSpecializationDTO().getId())
                    .stream()
                    .map(modelMapper::fromDoctorToDoctorDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public List<DoctorDTO> findAllDoctorsByVisitCriteria(VisitCriteriaHomePageDTO visitCriteriaHomePageDTOD) {
        try {
            if (visitCriteriaHomePageDTOD == null) {
                throw new NullPointerException("VISIT CRITERIA HOME PAGE IS NULL");
            }
            return doctorRepository.findAllByCityAndNameOrSpecialization(visitCriteriaHomePageDTOD.getCity().trim(), visitCriteriaHomePageDTOD.getNameOrSpecialization().trim())
                    .stream()
                    .map(modelMapper::fromDoctorToDoctorDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }
}
