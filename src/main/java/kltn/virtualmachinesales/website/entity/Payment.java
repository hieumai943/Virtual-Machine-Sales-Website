package kltn.virtualmachinesales.website.entity;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "payments")
@Entity
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="order_id")
    private Integer orderId;
    @Column(name="payment_method")
    private String paymentMethod;
    @Column(name="payment_date")
    private String paymentDate;
    private Integer status;
}
