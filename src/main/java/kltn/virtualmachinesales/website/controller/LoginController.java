package kltn.virtualmachinesales.website.controller;


import kltn.virtualmachinesales.website.http.DefaultResponse;
import kltn.virtualmachinesales.website.service.DockerMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@CrossOrigin
public class LoginController {

    @Autowired
    private DockerMonitorService dockerMonitorService;

    @GetMapping("/showMyLoginPage")
        public String showMyLoginPage() {
            return "plain-login";
        }

    @GetMapping("/test-docker/{port}")
    public ResponseEntity<DefaultResponse<String>> testDocker(@PathVariable Integer port) {
        // Gọi monitorContainer trong một thread riêng biệt
        return DefaultResponse.success(dockerMonitorService.scheduleMonitorTask(port, 10000));
    }
}
