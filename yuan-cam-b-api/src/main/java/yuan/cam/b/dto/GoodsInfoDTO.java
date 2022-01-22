package yuan.cam.b.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsInfoDTO {

    @ApiModelProperty(value = "主键id")
    @Min(1)
    private Integer id;

    @ApiModelProperty(value = "品牌名字")
    @NotEmpty
    private String brand;

    @ApiModelProperty(value = "商品类型")
    @Min(0)
    @NotNull
    private Byte type;

    @ApiModelProperty(value = "商品名称")
    @NotEmpty
    private String goodsName;

    @ApiModelProperty(value = "商品价格")
    @Min(0)
    @NotNull
    private BigDecimal price;

    @ApiModelProperty(value = "商品史低价格")
    private BigDecimal historicalLowestPrice;

    @ApiModelProperty(value = "商品图片url")
    @NotEmpty
    private String url;

    @ApiModelProperty(value = "商品图片的高度")
    @Min(0)
    @NotNull
    private Integer height;

    @ApiModelProperty(value = "商品图片的宽度")
    @Min(0)
    @NotNull
    private Integer width;

    @ApiModelProperty(value = "创建时间")
    private Integer createTime;

    @ApiModelProperty(value = "更新时间")
    private Integer updateTime;
}
