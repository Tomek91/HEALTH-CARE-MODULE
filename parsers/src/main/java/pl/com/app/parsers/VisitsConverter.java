package pl.com.app.parsers;

import pl.com.app.dto.VisitDTO;

import java.util.List;


public class VisitsConverter extends JsonConverter<List<VisitDTO>> {
    public VisitsConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
