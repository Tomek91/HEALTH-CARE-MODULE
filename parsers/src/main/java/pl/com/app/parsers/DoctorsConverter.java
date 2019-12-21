package pl.com.app.parsers;

import pl.com.app.dto.DoctorDTO;

import java.util.List;


public class DoctorsConverter extends JsonConverter<List<DoctorDTO>> {
    public DoctorsConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
