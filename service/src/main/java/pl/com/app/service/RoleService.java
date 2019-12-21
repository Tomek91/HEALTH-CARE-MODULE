package pl.com.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import pl.com.app.dto.RoleDTO;
import pl.com.app.exceptions.ExceptionCode;
import pl.com.app.exceptions.MyException;
import pl.com.app.repository.RoleRepository;
import pl.com.app.service.mappers.ModelMapper;

import javax.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    public Set<RoleDTO> getAllRoles() {
        try {
            return roleRepository.findAll()
                    .stream()
                    .map(modelMapper::fromRoleToRoleDTO)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public RoleDTO getOneRole(Long roleId) {
        try {
            if (roleId == null) {
                throw new NullPointerException("ROLE ID IS NULL");
            }

            return roleRepository
                    .findById(roleId)
                    .map(modelMapper::fromRoleToRoleDTO)
                    .orElseThrow(NullPointerException::new);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }
}
