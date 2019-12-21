package pl.com.app.parsers;

import pl.com.app.dto.AdviceDTO;

import java.util.List;

public class AdvicesConverter extends JsonConverter<List<AdviceDTO>> {
    public AdvicesConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
