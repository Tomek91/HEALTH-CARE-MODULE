package pl.com.app.validators;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pl.com.app.dto.AddressDTO;
import pl.com.app.exceptions.ExceptionCode;
import pl.com.app.exceptions.MyException;
import pl.com.app.service.AddressService;

@Component
@RequiredArgsConstructor
public class AddressValidator implements Validator {
    private final AddressService addressService;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(AddressDTO.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        if (o == null) {
            throw new MyException(ExceptionCode.VALIDATION, "OBJECT IS NULL");
        }

        AddressDTO addressDTO = (AddressDTO) o;

        if (StringUtils.isBlank(addressDTO.getCity()) || !addressDTO.getCity().matches("[A-Z ]+")) {
            errors.rejectValue("city", "City is not correct");
        }
        if (StringUtils.isBlank(addressDTO.getStreet()) || !addressDTO.getStreet().matches("[A-Z ]+")) {
            errors.rejectValue("street", "Street is not correct");
        }
        if (addressDTO.getNumber() == null || addressDTO.getNumber() < 0) {
            errors.rejectValue("number", "Number is not correct");
        }
        if (StringUtils.isBlank(addressDTO.getPostalCode()) || !addressDTO.getPostalCode().matches("\\d{2}-\\d{3}")) {
            errors.rejectValue("postalCode", "Postal code is not correct");
        }

        
        if (!errors.hasErrors()) {
            if (addressDTO.getId() == null || !addressService.getOneAddress(addressDTO.getId()).getStreet().equals(addressDTO.getStreet())){
                if (addressService.isNotUniqueAddressStreet(addressDTO.getStreet())){
                    errors.reject("street_unique", "STREET MUST BE UNIQUE");
                }
            }
        }
    }
}
