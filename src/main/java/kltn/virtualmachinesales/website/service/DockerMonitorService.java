package kltn.virtualmachinesales.website.service;


import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import com.github.dockerjava.api.model.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@EnableScheduling
@Slf4j
public class DockerMonitorService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private DockerClient dockerClient;

    @Value("${passwordUbuntu}")
    private String passwordUbuntu;

    @Value("${docker.container.name}")
    private String containerName;

    @Value("${docker.cpu.limit}")
    private double cpuLimit;

    @Value("${docker.memory.limit}")
    private long memoryLimit;

    private static final String NGINX_ALERT_URL = "http://localhost:81";

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    @PostConstruct
    public void init() {
        try {
            ApacheDockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                    .dockerHost(new URI("unix:///var/run/docker.sock"))
                    .maxConnections(100)
                    .connectionTimeout(Duration.ofSeconds(30))
                    .responseTimeout(Duration.ofSeconds(45))
                    .build();

            this.dockerClient = DockerClientBuilder.getInstance()
                    .withDockerHttpClient(httpClient)
                    .build();

            log.info("Docker client initialized successfully");
        } catch (Exception e) {
            log.error("Failed to initialize Docker client", e);
            // Có thể throw một custom exception ở đây nếu bạn muốn ngăn ứng dụng khởi động
        }
    }



    @Scheduled(fixedRate = 5000)  // Kiểm tra mỗi 5 giây
    public void monitorContainer() {
        String containerName = "hieuxfce2";

        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            String command = String.format("echo '%s' | sudo -S docker stats %s --no-stream --format \"{{.MemPerc}},{{.CPUPerc}}\"",
                    passwordUbuntu, containerName);

            processBuilder.command("bash", "-c", command);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            List<String> output = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                output.add(line);
            }

            int exitCode = process.waitFor();

            if (exitCode == 0 && !output.isEmpty()) {
                String[] stats = output.get(0).split(",");
                if (stats.length == 2) {
                    String memoryUsage = stats[0].trim();
                    String cpuUsage = stats[1].trim();
                    // Create a JSON object with the stats
                    Map<String,String> monitoringData = new HashMap<>();
                    monitoringData.put("memoryUsage", memoryUsage);
                    monitoringData.put("cpuUsage", cpuUsage);
                    // Send the JSON object via WebSocket
                    messagingTemplate.convertAndSend("/topic/docker-stats", monitoringData.toString());
                } else {
                    messagingTemplate.convertAndSend("/topic/docker-stats", "Unexpected output format");
                }
            } else {
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                StringBuilder errorMessage = new StringBuilder();
                while ((line = errorReader.readLine()) != null) {
                    errorMessage.append(line).append("\n");
                }
                messagingTemplate.convertAndSend("/topic/docker-stats", "Command failed: " + errorMessage.toString());
            }

        } catch (Exception e) {
            messagingTemplate.convertAndSend("/topic/docker-stats", "Error: " + e.getMessage());
        }
    }
}
