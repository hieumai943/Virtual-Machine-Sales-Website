package kltn.virtualmachinesales.website.controller;

import kltn.virtualmachinesales.website.entity.user.User;
import kltn.virtualmachinesales.website.repository.UserRepository;
import kltn.virtualmachinesales.website.service.RedisService;
import kltn.virtualmachinesales.website.service.SendEmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class EmailController {
    @Autowired
    private SendEmailService sendEmailService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/save")
    public void save(@RequestParam String key, @RequestParam String value) {
        redisService.save(key, value);
        redisService.setExpire(key, 10, TimeUnit.SECONDS);
    }

    @GetMapping("/get")
    public Object get(@RequestParam String key) {
        return redisService.find(key);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam String key) {
        redisService.delete(key);
    }

    @KafkaListener(topics = "sendEmail", groupId = "newEmail")
    public void sendEmail(String message, Acknowledgment acknowledgment) {
        try {
            String gmail = message.split(" ")[0];
            String username = message.split(" ")[1];
            sendEmailService.sendEmail(gmail, "Register", username);
            System.out.println(message);
            acknowledgment.acknowledge(); // Xác nhận tin nhắn đã được xử lý
        } catch (Exception e) {
            // Xử lý lỗi nếu cần
            log.info(e.getMessage());
        }
    }
}
