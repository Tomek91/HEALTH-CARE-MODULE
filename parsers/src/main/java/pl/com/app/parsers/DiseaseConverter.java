package pl.com.app.parsers;

import pl.com.app.dto.DiseaseDTO;

import java.util.List;


public class DiseaseConverter extends JsonConverter<List<DiseaseDTO>> {
    public DiseaseConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
