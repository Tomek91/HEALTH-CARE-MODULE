package pl.com.app.parsers;

import pl.com.app.dto.PatientDTO;

import java.util.List;


public class PatientsConverter extends JsonConverter<List<PatientDTO>> {
    public PatientsConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
