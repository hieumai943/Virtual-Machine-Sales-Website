package kltn.virtualmachinesales.website.controller.nginx_auth;

import kltn.virtualmachinesales.website.dto.request.VirtualMachineDTO;
import kltn.virtualmachinesales.website.entity.Machine;
import kltn.virtualmachinesales.website.entity.PortContainerMapping;
import kltn.virtualmachinesales.website.repository.MachineRepository;
import kltn.virtualmachinesales.website.request.AuthRequest;
import kltn.virtualmachinesales.website.service.NginxService;
import kltn.virtualmachinesales.website.service.implement.PortContainerMappingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.Inet4Address;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

@RestController
@CrossOrigin("http://localhost:3000/")
public class NginxAuthController {

    @Value("${nginx.htpasswd.path}")
    private String htpasswdPath;

    @Value("${passwordUbuntu}")
    private String passwordUbuntu;

    @Autowired
    private NginxService nginxService;
    @Autowired
    private PortContainerMappingServiceImpl portContainerMappingServiceImpl;
    @Autowired
    private MachineRepository machineRepository;

    @PostMapping("/change-auth")
    public String changeAuth(@RequestBody AuthRequest request ) {
        StringBuilder output = new StringBuilder();
        List<String> commands = List.of(
                "cd /home/hieunm369/Documents/kltn/XPRA_/app/ubuntu/nginx",
                "echo '"+ passwordUbuntu +"' | sudo -S htpasswd -b .htpasswd " + request.getUsername() + " " + request.getPassword(),
                "cd /home/hieunm369/Documents/'Virtual machine'/website/src/main/resources/nginx",
                "echo '"+ passwordUbuntu +"' | sudo -S htpasswd -b .htpasswd " + request.getUsername() + " " + request.getPassword()
        );

        try {
            // khi tao 1 tai khoan moi tu dong tao 1 container + port
            ProcessBuilder processBuilder = new ProcessBuilder();
            if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
                processBuilder.command("cmd.exe", "/c", String.join(" && ", commands));
            } else {
                processBuilder.command("bash", "-c", String.join(" && ", commands));
            }
            String htpasswdPath = "/home/hieunm369/Documents/kltn/XPRA_/app/ubuntu/nginx/.htpasswd";
            Path path = Paths.get(htpasswdPath);
            long lineCount = Files.lines(path).count();
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
            output.append("Exited with error code : ").append(exitCode).append("\n");
            // doc file .htpasswd
            path = Paths.get(htpasswdPath);
            if(Files.lines(path).count() != lineCount){
                // them container moi + port moi
                Machine machine  = machineRepository.findById(request.getMachineId()).get();

                PortContainerMapping portContainerMapping = new PortContainerMapping();
                portContainerMapping.setRam(Float.valueOf(machine.getRam()));
                portContainerMapping.setCpu(Float.valueOf(machine.getMemory()));
                portContainerMapping.setMachineId(request.getMachineId());
                portContainerMappingServiceImpl.create(portContainerMapping);
                output.append("container moi va port moi da duoc tao ").append("\n");
            } ;

        } catch (Exception e) {
            output.append("Error executing command: ").append(e.getMessage());
        }
    return output.toString();
    }
    @PostMapping("/send-email")
    public void sendEmail(@RequestBody AuthRequest request) {
        nginxService.sendEmail(request.getEmail(), request.getUsername());
    }
    @PostMapping("create/virtual-machine")
    public void createVirtualMachine(@RequestBody VirtualMachineDTO virtualMachineDTO) {
        nginxService.createVirtualMachine(virtualMachineDTO);
    }
}
