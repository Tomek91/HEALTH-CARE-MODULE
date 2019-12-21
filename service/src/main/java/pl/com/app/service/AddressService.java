package pl.com.app.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import pl.com.app.dto.AddressDTO;
import pl.com.app.exceptions.ExceptionCode;
import pl.com.app.exceptions.MyException;
import pl.com.app.model.Address;
import pl.com.app.repository.AddressRepository;
import pl.com.app.repository.WorkplaceRepository;
import pl.com.app.service.mappers.ModelMapper;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final WorkplaceRepository workplaceRepository;
    private final ModelMapper modelMapper;

    public void addAddress(AddressDTO addressDTO) {
        try {
            if (addressDTO == null) {
                throw new NullPointerException("ADDRESS IS NULL");
            }

            addressRepository.save(modelMapper.fromAddressDTOToAddress(addressDTO));
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public void modifyAddress(AddressDTO addressDTO) {
        try {
            if (addressDTO == null) {
                throw new NullPointerException("ADDRESS IS NULL");
            }

            Address address = addressRepository.findById(addressDTO.getId()).orElseThrow(NullPointerException::new);

            address.setCity(addressDTO.getCity() == null ? address.getCity() : addressDTO.getCity());
            address.setStreet(addressDTO.getStreet() == null ? address.getStreet() : addressDTO.getStreet());
            address.setPostalCode(addressDTO.getPostalCode() == null ? address.getPostalCode() : addressDTO.getPostalCode());
            address.setNumber(addressDTO.getNumber() == null ? address.getNumber() : addressDTO.getNumber());

            addressRepository.save(address);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public AddressDTO getOneAddress(Long addressId) {
        try {
            if (addressId == null) {
                throw new NullPointerException("ADDRESS ID IS NULL");
            }

            return addressRepository
                    .findById(addressId)
                    .map(modelMapper::fromAddressToAddressDTO)
                    .orElseThrow(NullPointerException::new);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public List<AddressDTO> getAllAddresses() {
        try {
            return addressRepository
                    .findAll()
                    .stream()
                    .map(modelMapper::fromAddressToAddressDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }

    public void deleteAddress(Long addressId) {
        try {
            if (addressId == null) {
                throw new NullPointerException("ADDRESS ID IS NULL");
            }
            if (workplaceRepository.existsByAddress_IdEquals(addressId)){
                throw new UnsupportedOperationException("CAN'T DELETE RECORD, IT USED IN WORKPLACE");
            }
            addressRepository.deleteById(addressId);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }


    public boolean isNotUniqueAddressStreet(String addressStreet) {
        try {
            if (addressStreet == null) {
                throw new NullPointerException("ADDRESS STREET IS NULL");
            }
            return addressRepository.existsByStreet(addressStreet);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }
}
