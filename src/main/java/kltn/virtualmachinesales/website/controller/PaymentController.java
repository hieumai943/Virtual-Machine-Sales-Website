package kltn.virtualmachinesales.website.controller;

import kltn.virtualmachinesales.website.dto.MachineDTO;
import kltn.virtualmachinesales.website.entity.Payment;
import kltn.virtualmachinesales.website.http.DefaultResponse;
import kltn.virtualmachinesales.website.service.MachineService;
import kltn.virtualmachinesales.website.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/machine/create")
    public ResponseEntity<DefaultResponse<Payment>> create(@RequestBody Payment payment ) {
        Payment payment1 =  paymentService.createPayment(payment);
        return DefaultResponse.success("Payment đã được tạo thành công", payment1);

    }
}
