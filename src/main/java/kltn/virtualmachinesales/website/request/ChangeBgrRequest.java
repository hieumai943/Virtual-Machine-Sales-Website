package kltn.virtualmachinesales.website.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ChangeBgrRequest {
    private MultipartFile file;
    private Integer port;

}
