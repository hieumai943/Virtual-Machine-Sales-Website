package kltn.virtualmachinesales.website.controller;

import kltn.virtualmachinesales.website.dto.MachineDTO;
import kltn.virtualmachinesales.website.entity.Payment;
import kltn.virtualmachinesales.website.http.DefaultResponse;
import kltn.virtualmachinesales.website.service.MachineService;
import kltn.virtualmachinesales.website.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/payment/create")
    public ResponseEntity<DefaultResponse<Payment>> create(@RequestBody Payment payment ) {
        Payment payment1 =  paymentService.createPayment(payment);
        return DefaultResponse.success("Payment đã được tạo thành công", payment1);

    }
    @GetMapping("/payment/get/{id}")
    public ResponseEntity<DefaultResponse<Payment>> get(@PathVariable Integer id) {
        Payment payment = paymentService.getPaymentById(id);
        return DefaultResponse.success(payment);
    }
}
