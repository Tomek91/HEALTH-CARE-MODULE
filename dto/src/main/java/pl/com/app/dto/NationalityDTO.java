package pl.com.app.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.com.app.model.ENationalityName;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NationalityDTO {
    private Long id;
    private ENationalityName name;
}
