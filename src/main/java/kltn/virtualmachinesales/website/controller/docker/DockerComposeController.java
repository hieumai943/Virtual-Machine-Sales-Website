package kltn.virtualmachinesales.website.controller.docker;

import kltn.virtualmachinesales.website.request.CreateContainerRequest;
import kltn.virtualmachinesales.website.service.DockerComposeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/api/docker-compose")
@CrossOrigin(origins = "http://localhost:3000")
public class DockerComposeController {
    @Autowired
    private DockerComposeService dockerComposeService;

    @PutMapping("/update")
    public ResponseEntity<String> updateConfig(@RequestBody CreateContainerRequest request) {
        try {
            String result = dockerComposeService.createUpdatedServiceResources( request.getCpuLimit(), request.getMemoryLimit(), request.getPort());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating configuration: " + e.getMessage());
        }
    }
}
