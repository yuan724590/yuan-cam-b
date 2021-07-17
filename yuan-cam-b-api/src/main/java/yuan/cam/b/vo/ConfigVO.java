package yuan.cam.b.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ConfigVO {

    @ApiModelProperty("商品id")
    private Integer id;

    @ApiModelProperty("商品品牌")
    private String brand;

    @ApiModelProperty("商品类型")
    private String type;

    @ApiModelProperty("商品名")
    private String goodsName;

    @ApiModelProperty("商品价格")
    private Integer price;

    @ApiModelProperty("底价")
    private Integer floorPrice;

}
