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

    @ApiModelProperty(value = "商品品牌名")
    @NotEmpty
    private String goodsBrand;

    @ApiModelProperty(value = "商品类型 1-CPU 2-主板 3-显卡 4-显示器 5-内存条 6-硬盘")
    @Min(0)
    @NotNull
    private Byte goodsType;

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
    private String imageUrl;

    @ApiModelProperty(value = "商品图片的高度")
    @Min(0)
    @NotNull
    private Integer imageHeight;

    @ApiModelProperty(value = "商品图片的宽度")
    @Min(0)
    @NotNull
    private Integer imageWidth;

    @ApiModelProperty(value = "删除状态 0-未删除 1-已删除")
    private Byte deleted;

    @ApiModelProperty(value = "创建时间")
    private Integer createTime;

    @ApiModelProperty(value = "更新时间")
    private Integer updateTime;
}
