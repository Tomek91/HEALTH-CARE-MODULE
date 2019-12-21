package pl.com.app.parsers;

import pl.com.app.dto.AddressDTO;

import java.util.List;

public class AddressesConverter extends JsonConverter<List<AddressDTO>> {
    public AddressesConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
