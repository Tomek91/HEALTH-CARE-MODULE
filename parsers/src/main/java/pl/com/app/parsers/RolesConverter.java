package pl.com.app.parsers;

import pl.com.app.dto.RoleDTO;

import java.util.List;

public class RolesConverter extends JsonConverter<List<RoleDTO>> {
    public RolesConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
