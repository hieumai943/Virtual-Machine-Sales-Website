package kltn.virtualmachinesales.website.controller.nginx_auth;

import kltn.virtualmachinesales.website.dto.request.VirtualMachineDTO;
import kltn.virtualmachinesales.website.entity.Machine;
import kltn.virtualmachinesales.website.entity.PortContainerMapping;
import kltn.virtualmachinesales.website.repository.MachineRepository;
import kltn.virtualmachinesales.website.repository.PortContainerMappingRepository;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

import static kltn.virtualmachinesales.website.service.DockerComposeService.available;

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
    @Autowired
    private PortContainerMappingRepository portContainerMappingRepository;

    @PostMapping("/shop/change-auth")
    public String changeAuth(@RequestBody AuthRequest request ) {
        StringBuilder output = new StringBuilder();
        List<Integer> ports = portContainerMappingRepository.findAllPorts();
        Integer maxPort = ports.stream().max(Integer::compareTo).get() + 1;
        while(!available(maxPort)){
            maxPort++;
        }
        try {
            List<String> commands = new ArrayList<>();
            String newHtpasswdFile = "nginx" + maxPort + ".htpasswd";
            String htpasswdFolderPath = "/home/hieunm369/Documents/'Virtual machine'/website/config/nginx/htpasswd_folder";

            commands.add("cd " + htpasswdFolderPath);
            commands.add("touch " + newHtpasswdFile);
            commands.add("echo '" + passwordUbuntu + "' | sudo -S htpasswd -b " + newHtpasswdFile + " " + request.getUsername() + " " + request.getPassword());
            // khi tao 1 tai khoan moi tu dong tao 1 container + port
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
            output.append("Exited with error code : ").append(exitCode).append("\n");
            // doc file nginx91.htpasswd

//            if(isExisted){
//                output.append("username đã tồn tại").append("\n");
//            }
//            else if(Files.lines(path).count() != lineCount) {
                // them container moi + port moi
                Machine machine = machineRepository.findById(request.getMachineId()).get();

                PortContainerMapping portContainerMapping = new PortContainerMapping();
                portContainerMapping.setRam(Float.valueOf(machine.getRam()));
                portContainerMapping.setCpu(Float.valueOf(machine.getMemory()));
                portContainerMapping.setMachineId(request.getMachineId());
                portContainerMapping.setPort(maxPort);
                portContainerMapping.setImgSrc("https://res.cloudinary.com/dlggnttqv/image/upload/v1726463019/machine_1_fgtptn.png");
                portContainerMapping.setCreated(new Date());
                portContainerMapping.setExpired(new Date());
                portContainerMappingServiceImpl.create(portContainerMapping);
                output.append("container moi va port moi da duoc tao ").append("\n");
                output.append("port using:"+ maxPort).append("\n");
//            }
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
