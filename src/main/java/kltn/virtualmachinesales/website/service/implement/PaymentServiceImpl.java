package kltn.virtualmachinesales.website.service.implement;

import kltn.virtualmachinesales.website.entity.Payment;
import kltn.virtualmachinesales.website.repository.PaymentRepository;
import kltn.virtualmachinesales.website.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Payment createPayment(Payment payment){
            paymentRepository.save(payment);
            return payment;
    }

    @Override
    public Payment getPaymentById(Integer  paymentId){
         return  paymentRepository.findById(paymentId).orElse(null);

    }
}
