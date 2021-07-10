package yuan.cam.b.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigDTO {
    private Integer id;

    private String brand;

    private String type;

    private String name;

    private Double price;

    private double floorPrice;
}
