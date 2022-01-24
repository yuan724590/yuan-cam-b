package yuan.cam.b.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandCountVO {

    @ApiModelProperty(value = "品牌数量信息")
    private List<BrandCountInfoVO> list;

    @Data
    @AllArgsConstructor
    @ApiModel("BrandCountVO.BrandCountInfoVO")
    public static class BrandCountInfoVO {

        @ApiModelProperty("商品品牌名")
        private String goodsBrand;

        @ApiModelProperty("数量")
        private Integer count;
    }
}
