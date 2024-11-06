package kltn.virtualmachinesales.website.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "port_container_mapping")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

public class PortContainerMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "container_name")
    private String containerName;
    private Integer port;
    private Float ram;
    private Float cpu;
    private Integer machineId;
    private Boolean status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date expired;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date created;
    @Column(name = "img_src")
    private String imgSrc;
}
