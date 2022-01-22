package yuan.cam.b.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ComputerConfigVO {

    @ApiModelProperty("商品id")
    private Integer id;

    @ApiModelProperty("商品名称")
    private String brand;

    @ApiModelProperty("商品类型")
    private String type;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("商品价格")
    private Double price;

    @ApiModelProperty("底价")
    private Double floorPrice;

    @ApiModelProperty(value = "商品史低价格")
    private BigDecimal historicalLowestPrice;

    @ApiModelProperty("创建时间")
    private Integer createTime;

    @ApiModelProperty("更新时间")
    private Integer updateTime;
}
