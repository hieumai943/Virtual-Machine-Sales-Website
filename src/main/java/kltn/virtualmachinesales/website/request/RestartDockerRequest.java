package kltn.virtualmachinesales.website.request;

import lombok.Data;

@Data
public class RestartDockerRequest {
    private String containerName;
}
