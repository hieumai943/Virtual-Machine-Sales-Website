package kltn.virtualmachinesales.website.entity;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;

import java.util.Date;

@Table(name = "order_machine")
@Entity
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="machine_id")
    private Integer machineId;
    @Column(name="user_id")
    private Integer userId;
    private Double price;
    @Column(name="core_cpu")
    private Integer status;
    @Column(name="created_at")
    private Date createdAt;
}
