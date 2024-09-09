package kltn.virtualmachinesales.website.entity;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "machines")
@Entity
@Data
public class Machine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;

}
