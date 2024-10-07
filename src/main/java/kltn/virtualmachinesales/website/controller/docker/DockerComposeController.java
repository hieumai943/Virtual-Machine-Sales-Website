package kltn.virtualmachinesales.website.controller.docker;

import kltn.virtualmachinesales.website.service.DockerComposeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/docker-compose")
public class DockerComposeController {
    @Autowired
    private DockerComposeService dockerComposeService;

    @PutMapping("/update")
    public ResponseEntity<String> updateConfig(@RequestBody Map<String, String> updates) {
        try {
            String result = dockerComposeService.updateConfig(updates);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating configuration: " + e.getMessage());
        }
    }
}
