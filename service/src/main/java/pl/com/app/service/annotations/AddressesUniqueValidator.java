package pl.com.app.service.annotations;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import pl.com.app.service.AddressService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class AddressesUniqueValidator implements ConstraintValidator<AAddressUnique, String> {
    private final AddressService addressService;

    public void initialize(AAddressUnique constraint) {
    }

    public boolean isValid(String addressStreet, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(addressStreet) || addressService.isNotUniqueAddressStreet(addressStreet)) {
            return false;
        }
        return true;
    }
}
