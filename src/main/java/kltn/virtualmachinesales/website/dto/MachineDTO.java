package kltn.virtualmachinesales.website.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MachineDTO {
    private int id;
    private String name;
    private String description;
    private String ram;
    private String coreCpu;
    private String memory;
    private String imgSrc;
    private Boolean isSample;
    private String oldPrice;
    private String newPrice;
    private Integer userId;
}
