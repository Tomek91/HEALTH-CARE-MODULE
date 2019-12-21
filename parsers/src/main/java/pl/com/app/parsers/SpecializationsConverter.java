package pl.com.app.parsers;

import pl.com.app.dto.SpecializationDTO;

import java.util.List;


public class SpecializationsConverter extends JsonConverter<List<SpecializationDTO>> {
    public SpecializationsConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
