package kltn.virtualmachinesales.website.service;

import kltn.virtualmachinesales.website.entity.Machine;
import kltn.virtualmachinesales.website.entity.PortContainerMapping;
import kltn.virtualmachinesales.website.repository.MachineRepository;
import kltn.virtualmachinesales.website.repository.PortContainerMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;

import java.net.ConnectException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DockerComposeService {
    private static final String DOCKER_COMPOSE_FILE = "config/docker-compose.yaml";
    private static final String CONFIG_NGINX_FOLDER = "config/nginx";

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private PortContainerMappingRepository portContainerMappingRepository;

    @Value("${passwordUbuntu}")
    private String passwordUbuntu;
    @Autowired
    private MachineRepository machineRepository;

    public static boolean available(int port) throws IllegalStateException {
        try (Socket ignored = new Socket("localhost", port)) {
            return false;
        } catch (ConnectException e) {
            return true;
        } catch (IOException e) {
            throw new IllegalStateException("Error while trying to check open port", e);
        }
    }
    public String createUpdatedServiceResources(String cpuLimit, String memoryLimit, Integer maxPort, Integer machineId) {
        try {
            String serviceName = "nginx" + maxPort ;
            String port = maxPort + ":80";
            // Ensure the config/nginx folder exists
            Files.createDirectories(Paths.get(CONFIG_NGINX_FOLDER));

            File dockerComposeFile = new File(DOCKER_COMPOSE_FILE);
            Map<String, Object> composeConfig;

            // Read the existing docker-compose.yml file
            try (InputStream inputStream = new FileInputStream(dockerComposeFile)) {
                Yaml yaml = new Yaml();
                composeConfig = yaml.load(inputStream);
            }

            // Get the services section
            Map<String, Object> services = (Map<String, Object>) composeConfig.get("services");

            if (services == null || !services.containsKey("nginx1")) {
                return "Error: nginx1 service not found in the original file.";
            }

            // Get the nginx1 service configuration
            Map<String, Object> nginx1Config = (Map<String, Object>) services.get("nginx1");
            Map<String, Object> xpra = (Map<String, Object>) services.get("xpra");
            Map<String, Object> newXpra = new HashMap<>(xpra);
            newXpra.put("container_name", "hieuxfce" + maxPort);
            services.put("xpra"+maxPort, newXpra);
            services.remove("xpra");
            // Create a new service configuration based on nginx1
            Map<String, Object> newService = new HashMap<>(nginx1Config);
            List<String> allPorts = new ArrayList<>();
            List<String> volumes = new ArrayList<>();
            volumes.add("./nginx.conf:/etc/nginx/nginx.conf");
            volumes.add("./htpasswd_folder/nginx"+maxPort+".htpasswd:/etc/nginx/.htpasswd");
            allPorts.add(port);
            newService.put("container_name", serviceName);
            newService.put("ports", allPorts);
            newService.put("volumes", volumes);
            List<String> dependXpra = new ArrayList<>();
            dependXpra.add("xpra"+maxPort);
            newService.put("depends_on",dependXpra);
            // Update the deploy > resources > limits section with new limits
            Map<String, Object> deploy = (Map<String, Object>) newService.get("deploy");
            if (deploy == null) {
                deploy = new HashMap<>();
                newService.put("deploy", deploy);
            }
            Map<String, Object> resources = (Map<String, Object>) deploy.get("resources");
            if (resources == null) {
                resources = new HashMap<>();
                deploy.put("resources", resources);
            }
            Map<String, Object> limits = (Map<String, Object>) resources.get("limits");
            if (limits == null) {
                limits = new HashMap<>();
                resources.put("limits", limits);
            }
            limits.put("cpus", cpuLimit);
            limits.put("memory", memoryLimit);

            // Remove the old nginx1 service
            services.remove("nginx1");

            // Add the new service
            services.put(serviceName, newService);
            // Create a new file name with timestamp
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String newFileName = "docker-compose_" + timestamp + ".yml";
            File newFile = new File(CONFIG_NGINX_FOLDER, newFileName);

            // Write the updated configuration to the new file
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            options.setPrettyFlow(true);
            Yaml yamlWriter = new Yaml(options);

            try (FileWriter writer = new FileWriter(newFile)) {
                yamlWriter.dump(composeConfig, writer);
            }

            return restartDocker(newFileName, port, machineId);
        } catch (IOException e) {
            e.printStackTrace();
            return "Error updating limits";
        }
    }
    private String restartDocker(String fileName, String port, Integer machineId) {
        StringBuilder output = new StringBuilder();
        List<String> commands = List.of(
                "cd /home/hieunm369/Documents/'Virtual machine'/website/config/nginx",
                "echo '"+ passwordUbuntu +"' | sudo -S docker compose -f " + fileName+" up -d"
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
            output.append("\nExited with error code : ").append(exitCode);
            output.append("\nport using: ").append(port);
            if(exitCode == 0){
                Machine machine = machineRepository.findById(machineId).orElse(null);
                machine.setStatus(Boolean.TRUE);
                machineRepository.save(machine);
                List<PortContainerMapping> portContainerMappings = portContainerMappingRepository.findAllByMachineId(machineId);
                portContainerMappings.forEach(x -> x.setStatus(Boolean.TRUE));
                portContainerMappingRepository.saveAll(portContainerMappings);
            }
        } catch (Exception e) {
            output.append("Error executing command: ").append(e.getMessage());
        }

        return output.toString();
    }


}
