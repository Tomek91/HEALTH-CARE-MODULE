package pl.com.app.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import pl.com.app.dto.NationalityDTO;
import pl.com.app.exceptions.ExceptionCode;
import pl.com.app.exceptions.MyException;
import pl.com.app.repository.NationalityRepository;
import pl.com.app.service.mappers.ModelMapper;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class NationalityService {
    private final NationalityRepository nationalityRepository;
    private final ModelMapper modelMapper;

    public void addNationality(NationalityDTO nationalityDTO){
        try {
            if (nationalityDTO == null) {
                throw new NullPointerException("NATIONALITY IS NULL");
            }

            nationalityRepository.save(modelMapper.fromNationalityDTOToNationality(nationalityDTO));
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public List<NationalityDTO> getAllNationalities() {
        try {
            return nationalityRepository
                    .findAll()
                    .stream()
                    .map(modelMapper::fromNationalityToNationalityDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }
}
