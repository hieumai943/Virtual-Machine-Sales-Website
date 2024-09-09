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

@RestController
public class NginxAuthController {

    @Value("${nginx.htpasswd.path}")
    private String htpasswdPath;

    @PostMapping("/change-auth")
    public String changeAuth(@RequestBody AuthRequest request) {
        //            String command = String.format("htpasswd -c %s %s %s",
//                    htpasswdPath, request.getUsername(), request.getPassword());
        StringBuilder output = new StringBuilder();
        String command = "cd /home/hieunm369/Documents/kltn/XPRA_/app/ubuntu/nginx";
        String changePassCommand = "sudo htpasswd -b .htpasswd hieumai943 123456";
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
                processBuilder.command("cmd.exe", "/c", command);
            } else {
                processBuilder.command("bash", "-c", command);
            }

            Process process = processBuilder.start();
            process = Runtime.getRuntime().exec(changePassCommand);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            output.append("Exited with error code : ").append(exitCode);

        } catch (Exception e) {
            output.append("Error executing command: ").append(e.getMessage());
        }

        return output.toString();
        }
    }
