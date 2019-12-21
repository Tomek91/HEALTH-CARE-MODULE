package pl.com.app.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import pl.com.app.dto.DiseaseDTO;
import pl.com.app.exceptions.ExceptionCode;
import pl.com.app.exceptions.MyException;
import pl.com.app.repository.DiseaseRepository;
import pl.com.app.service.mappers.ModelMapper;

import javax.transaction.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class DiseaseService {
    private final DiseaseRepository diseaseRepository;
    private final ModelMapper modelMapper;

    public boolean isNotUniqueDiseaseName(String diseaseName) {
        try {
            if (diseaseName == null) {
                throw new NullPointerException("DISEASE NAME IS NULL");
            }
            return diseaseRepository.existsByName(diseaseName);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public DiseaseDTO getOneDisease(Long diseaseId) {
        try {
            if (diseaseId == null) {
                throw new NullPointerException("DISEASE ID IS NULL");
            }
            return diseaseRepository
                    .findById(diseaseId)
                    .map(modelMapper::fromDiseaseToDiseaseDTO)
                    .orElseThrow(NullPointerException::new);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }
}
