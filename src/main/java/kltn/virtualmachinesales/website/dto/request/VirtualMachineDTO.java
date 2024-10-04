package kltn.virtualmachinesales.website.dto.request;

import lombok.Data;

@Data
public class VirtualMachineDTO {
    private Integer limitCpu;
    private Integer limitMemory;
    private Integer portHost;
}
