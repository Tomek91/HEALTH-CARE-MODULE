package pl.com.app.parsers;

import pl.com.app.dto.PatientCardDTO;

import java.util.List;


public class PatientCardsConverter extends JsonConverter<List<PatientCardDTO>> {
    public PatientCardsConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
