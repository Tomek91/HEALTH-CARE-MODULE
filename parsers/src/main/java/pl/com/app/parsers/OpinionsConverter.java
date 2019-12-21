package pl.com.app.parsers;

import pl.com.app.dto.OpinionDTO;

import java.util.List;


public class OpinionsConverter extends JsonConverter<List<OpinionDTO>> {
    public OpinionsConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
