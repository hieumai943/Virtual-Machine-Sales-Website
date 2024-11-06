package kltn.virtualmachinesales.website.dto.response;

import jakarta.persistence.Column;
import lombok.*;


@Data
@NoArgsConstructor
public class MachineDto {
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
    private Integer port;
}
