package kltn.virtualmachinesales.website.controller.nginx_auth;

import kltn.virtualmachinesales.website.request.AuthRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@RestController
public class NginxAuthController {

    @Value("${nginx.htpasswd.path}")
    private String htpasswdPath;

    @Value("${passwordUbuntu}")
    private String passwordUbuntu;

    @PostMapping("/change-auth")
    public String changeAuth(@RequestBody AuthRequest request) {
        StringBuilder output = new StringBuilder();
        List<String> commands = List.of(
                "cd /home/hieunm369/Documents/kltn/XPRA_/app/ubuntu/nginx",
                "echo '"+ passwordUbuntu +"' | sudo -S htpasswd -b .htpasswd " + request.getUsername() + " " + request.getPassword()
        );

        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
                processBuilder.command("cmd.exe", "/c", String.join(" && ", commands));
            } else {
                processBuilder.command("bash", "-c", String.join(" && ", commands));
            }
            Process process = processBuilder.start();
            // Đọc output tiêu chuẩn
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append("stdout: ").append(line).append("\n");
            }

            // Đọc lỗi tiêu chuẩn
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                output.append("stderr: ").append(line).append("\n");
            }

            int exitCode = process.waitFor();
            output.append("Exited with error code : ").append(exitCode);
        } catch (Exception e) {
            output.append("Error executing command: ").append(e.getMessage());
        }

        return output.toString();
        }
    }
