package kltn.virtualmachinesales.website.service;

import kltn.virtualmachinesales.website.entity.Payment;

public interface PaymentService {
    Payment createPayment(Payment payment);
    Payment getPaymentById(Integer paymentId);
}
