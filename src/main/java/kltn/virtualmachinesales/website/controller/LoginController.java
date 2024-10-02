package kltn.virtualmachinesales.website.controller;


import kltn.virtualmachinesales.website.http.DefaultResponse;
import kltn.virtualmachinesales.website.service.DockerMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@CrossOrigin
public class LoginController {

    @Autowired
    private DockerMonitorService dockerMonitorService;

    @GetMapping("/showMyLoginPage")
        public String showMyLoginPage() {
            return "plain-login";
        }

    @GetMapping("/test-docker")
    public ResponseEntity<DefaultResponse<String>> testDocker() {
        // Gọi monitorContainer trong một thread riêng biệt
        new Thread(() -> dockerMonitorService.monitorContainer()).start();
        return DefaultResponse.success("Monitoring started");
    }
}
