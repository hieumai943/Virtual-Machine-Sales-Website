package kltn.virtualmachinesales.website.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public class CreateContainerRequest {
        private String cpuLimit;
        private String memoryLimit;
    }
