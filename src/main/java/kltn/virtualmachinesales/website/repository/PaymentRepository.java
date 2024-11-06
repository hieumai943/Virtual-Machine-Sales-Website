package kltn.virtualmachinesales.website.repository;

import kltn.virtualmachinesales.website.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

}
