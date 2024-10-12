package kltn.virtualmachinesales.website.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import org.yaml.snakeyaml.constructor.Constructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DockerComposeService {
    private static final String DOCKER_COMPOSE_FILE = "config/docker-compose.yaml";

    @Autowired
    private ResourceLoader resourceLoader;

    @Value("${passwordUbuntu}")
    private String passwordUbuntu;

    public String updateConfig(Map<String, String> request) throws Exception {
        String serviceName  = "xpra";
        String cpuLimit = request.get("cpu_limit");
        String memoryLimit = request.get("memory_limit");
        String newPort = request.get("port");

        if (serviceName == null || cpuLimit == null || memoryLimit == null) {
            return "Invalid input";
        }

        try {
            // Load the docker-compose.yml file from the config folder
            File dockerComposeFile = new File(DOCKER_COMPOSE_FILE);

            try (InputStream inputStream = new FileInputStream(dockerComposeFile)) {
                // Read the docker-compose.yml file
                LoaderOptions loaderOptions = new LoaderOptions();
                Constructor constructor = new Constructor(loaderOptions);
                Yaml yaml = new Yaml(constructor);
                Map<String, Object> composeData = yaml.load(inputStream);

                // Update the limits
                Map<String, Object> services = (Map<String, Object>) composeData.get("services");
                if (services.containsKey(serviceName)) {
                    Map<String, Object> xpra = (Map<String, Object>) services.get(serviceName);
                    Map<String, Object> deploy = (Map<String, Object>) xpra.get("deploy");
                    Map<String, Object> resources = (Map<String, Object>) deploy.get("resources");
                    Map<String, Object> limits = (Map<String, Object>) resources.get("limits");
                    Map<String, Object> ports = (Map<String, Object>) services.get("nginx");
                    limits.put("cpus", cpuLimit);
                    limits.put("memory", memoryLimit);
//                    List<String> allPorts = (List<String>) ports.get("ports");
//                    if(!allPorts.contains(newPort)) {
//                        allPorts.add(newPort);
//                    }
//                    ports.put("ports", allPorts);
                    // Write the updated data back to the docker-compose.yml file
                    DumperOptions options = new DumperOptions();
                    options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
                    Yaml yamlWriter = new Yaml(options);
                    try (FileWriter writer = new FileWriter(dockerComposeFile)) {
                        yamlWriter.dump(composeData, writer);
                    }
                    restartDocker();
                    return restartDocker();
                } else {
                    return "Service not found";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error updating limits";
        }
    }
    private String restartDocker() {
        StringBuilder output = new StringBuilder();
        List<String> commands = List.of(
                "cd /home/hieunm369/Documents/'Virtual machine'/website/config",
                "echo '"+ passwordUbuntu +"' | sudo -S docker compose down",
                "echo '"+ passwordUbuntu +"' | sudo -S docker compose up -d"
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
