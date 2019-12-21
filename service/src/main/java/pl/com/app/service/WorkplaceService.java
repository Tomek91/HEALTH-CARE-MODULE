package pl.com.app.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import pl.com.app.dto.WorkplaceDTO;
import pl.com.app.exceptions.ExceptionCode;
import pl.com.app.exceptions.MyException;
import pl.com.app.model.Address;
import pl.com.app.model.Workplace;
import pl.com.app.repository.AddressRepository;
import pl.com.app.repository.DoctorRepository;
import pl.com.app.repository.WorkplaceRepository;
import pl.com.app.service.mappers.ModelMapper;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class WorkplaceService {
    private final WorkplaceRepository workplaceRepository;
    private final DoctorRepository doctorRepository;
    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;

    public void addWorkplace(WorkplaceDTO workplaceDTO){
        try {
            if (workplaceDTO == null) {
                throw new NullPointerException("WORKPLACE IS NULL");
            }

            if (workplaceDTO.getAddressDTO() == null) {
                throw new NullPointerException("ADDRESS IS NULL");
            }

            if (workplaceDTO.getAddressDTO().getId() == null) {
                throw new NullPointerException("ADDRESS ID IS NULL");
            }

            Address address = addressRepository
                    .findById(workplaceDTO.getAddressDTO().getId())
                    .orElseThrow(NullPointerException::new);

            Workplace workplace = modelMapper.fromWorkplaceDTOToWorkplace(workplaceDTO);
            workplace.setAddress(address);
            workplaceRepository.save(workplace);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public void modifyWorkplace(WorkplaceDTO workplaceDTO) {
        try {
            if (workplaceDTO == null) {
                throw new NullPointerException("WORKPLACE IS NULL");
            }

            if (workplaceDTO.getAddressDTO() == null) {
                throw new NullPointerException("ADDRESS IS NULL");
            }

            if (workplaceDTO.getAddressDTO().getId() == null) {
                throw new NullPointerException("ADDRESS ID IS NULL");
            }

            Address address = addressRepository
                    .findById(workplaceDTO.getAddressDTO().getId())
                    .orElseThrow(NullPointerException::new);

            Workplace workplace = workplaceRepository
                    .findById(workplaceDTO.getId())
                    .orElseThrow(NullPointerException::new);

            workplace.setAddress(address);
            workplace.setDescription(workplaceDTO.getDescription() == null ? workplace.getDescription() : workplaceDTO.getDescription());
            workplace.setName(workplaceDTO.getName() == null ? workplace.getName() : workplaceDTO.getName());

            workplaceRepository.save(workplace);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public WorkplaceDTO getOneWorkplace(Long workplaceId) {
        try {
            if (workplaceId == null) {
                throw new NullPointerException("WORKPLACE ID IS NULL");
            }

            return workplaceRepository
                    .findById(workplaceId)
                    .map(modelMapper::fromWorkplaceToWorkplaceDTO)
                    .orElseThrow(NullPointerException::new);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public List<WorkplaceDTO> getAllWorkplaces() {
        try {
            return workplaceRepository
                    .findAll()
                    .stream()
                    .map(modelMapper::fromWorkplaceToWorkplaceDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public void deleteWorkplace(Long workplaceId) {
        try {
            if (workplaceId == null) {
                throw new NullPointerException("WORKPLACE ID IS NULL");
            }
            if (doctorRepository.existsByWorkplace_IdEquals(workplaceId)){
                throw new UnsupportedOperationException("CAN'T DELETE RECORD, IT USED IN DOCTOR");
            }
            workplaceRepository.deleteById(workplaceId);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }


    public boolean isNotUniqueWorkplaceName(String workplaceName) {
        try {
            if (workplaceName == null) {
                throw new NullPointerException("WORKPLACE NAME IS NULL");
            }
            return workplaceRepository.existsByName(workplaceName);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }
}
