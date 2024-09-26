package kltn.virtualmachinesales.website.controller;


import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import com.github.dockerjava.api.model.Statistics;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static com.mysql.cj.conf.PropertyKey.logger;

@Service
@EnableScheduling
@Slf4j
public class DockerMonitorService {
    private DockerClient dockerClient;

    @Value("${passwordUbuntu}")
    private String passwordUbuntu;

    @Value("${docker.container.name}")
    private String containerName;

    @Value("${docker.cpu.limit}")
    private double cpuLimit;

    @Value("${docker.memory.limit}")
    private long memoryLimit;

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



//    @Scheduled(fixedRate = 50000)  // Kiểm tra mỗi 5 giây
    public void monitorContainer() {
        String containerName = "hieuxfce2";

        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            String command = String.format("echo '%s' | sudo -S docker stats %s --no-stream --format \"{{.MemUsage}},{{.CPUPerc}}\"",
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

                    System.out.println("Memory Usage: " + memoryUsage);
                    System.out.println("CPU Usage: " + cpuUsage);

                    // Ở đây bạn có thể thêm logic để phân tích và xử lý các giá trị này
                    // Ví dụ: chuyển đổi "100MiB / 1GiB" thành số để so sánh
                } else {
                    System.out.println("Unexpected output format");
                }
            } else {
                System.out.println("Command failed with exit code: " + exitCode);
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                while ((line = errorReader.readLine()) != null) {
                    System.out.println("Error: " + line);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private double calculateCpuUsage(Statistics stats) {
        // Tính toán CPU usage dựa trên thống kê từ Docker API
        // Đây là một cách tính đơn giản, bạn có thể cần điều chỉnh để có kết quả chính xác hơn
        long cpuDelta = stats.getCpuStats().getCpuUsage().getTotalUsage() - stats.getPreCpuStats().getCpuUsage().getTotalUsage();
        long systemDelta = stats.getCpuStats().getSystemCpuUsage() - stats.getPreCpuStats().getSystemCpuUsage();
        long numCpus = stats.getCpuStats().getOnlineCpus();

        return (cpuDelta / systemDelta) * numCpus * 100.0;
    }

    private void showWarning(long usedMemory, double usedCpu) {
        System.out.println("CẢNH BÁO: Tài nguyên container vượt quá giới hạn!");
        System.out.printf("Bộ nhớ sử dụng: %.2f GB (Giới hạn: 1 GB)%n", usedMemory / (1024.0 * 1024 * 1024));
        System.out.printf("CPU sử dụng: %.2f%% (Giới hạn: 100%%)%n", usedCpu);

        // Ở đây bạn có thể thêm mã để hiển thị pop-up cảnh báo
        // Ví dụ: gửi thông báo đến một ứng dụng frontend hoặc sử dụng một thư viện để hiển thị pop-up trên desktop
    }
}
