package pl.com.app.parsers;

import pl.com.app.dto.WorkplaceDTO;

import java.util.List;


public class WorkplacesConverter extends JsonConverter<List<WorkplaceDTO>> {
    public WorkplacesConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
