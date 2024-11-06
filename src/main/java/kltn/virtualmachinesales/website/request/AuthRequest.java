package kltn.virtualmachinesales.website.request;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
    private String email;
    private Integer machineId;
}
