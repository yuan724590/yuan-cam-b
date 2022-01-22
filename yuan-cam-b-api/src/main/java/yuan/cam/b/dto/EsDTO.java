package yuan.cam.b.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class EsDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel("ComputerConfigDTO.ESDeleteGoodsDTO")
    public static class ESDeleteGoodsDTO {

        @ApiModelProperty(value = "要删除的id列表", required = true)
        @NotEmpty
        private List<Integer> idList;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel("ComputerConfigDTO.ESQueryGoodsDTO")
    public static class ESQueryGoodsDTO {

        @ApiModelProperty(value = "主键id列表", required = true)
        private List<Integer> idList;

        @ApiModelProperty(value = "商品名字", required = true)
        private String goodsName;

        @NotNull
        @Min(1)
        @ApiModelProperty(value = "页码", required = true)
        private Integer page;

        @NotNull
        @Min(1)
        @ApiModelProperty(value = "页面大小", required = true)
        private Integer size;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel("ComputerConfigDTO.QueryIsExistByIdDTO")
    public static class QueryIsExistByIdDTO {

        @ApiModelProperty(value = "商品ID", required = true)
        private Integer id;
    }
}
