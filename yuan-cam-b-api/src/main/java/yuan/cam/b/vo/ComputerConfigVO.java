package yuan.cam.b.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ComputerConfigVO {

    @ApiModelProperty("商品id")
    private Integer id;

    @ApiModelProperty("商品品牌名")
    private String goodsBrand;

    @ApiModelProperty(value = "商品类型 1-CPU 2-主板 3-显卡 4-显示器 5-内存条 6-硬盘")
    private Byte goodsType;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("商品价格")
    private Double price;

    @ApiModelProperty(value = "商品史低价格")
    private BigDecimal historicalLowestPrice;

    @ApiModelProperty(value = "商品图片url")
    private String imageUrl;

    @ApiModelProperty(value = "商品图片的高度")
    private Integer imageHeight;

    @ApiModelProperty(value = "商品图片的宽度")
    private Integer imageWidth;

    @ApiModelProperty(value = "删除状态 0-未删除 1-已删除")
    private Byte deleted;

    @ApiModelProperty("创建时间")
    private Integer createTime;

    @ApiModelProperty("更新时间")
    private Integer updateTime;
}
