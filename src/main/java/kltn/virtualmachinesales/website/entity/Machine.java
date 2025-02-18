package kltn.virtualmachinesales.website.entity;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "machines")
@Entity
@Data
public class Machine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Integer id;
    private String name;
    private String description;
    private String ram;
    @Column(name="core_cpu")
    private String coreCpu;
    private String memory;
    @Column(name="img_src")
    private String imgSrc;
    @Column(name="is_Sample")
    private Boolean isSample;
    @Column(name="old_price")
    private String oldPrice;
    @Column(name="new_price")
    private String newPrice;
    @Column(name="user_id")
    private Integer userId;
    private Boolean status;
    @Column(name="user_auth")
    private String userAuth;
    @Column(name="pass_auth")
    private String passAuth;
}
