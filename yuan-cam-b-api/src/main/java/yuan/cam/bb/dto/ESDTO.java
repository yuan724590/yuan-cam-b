package yuan.cam.bb.dto;

import com.alibaba.fastjson.JSONObject;
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

public class ESDTO {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel("ComputerConfigDTO.ESInsertConfigDTO")
    public static class ESInsertConfigDTO{
        @ApiModelProperty(value = "新增对象", required = true)
        private ConfigDTO configDTO;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel("ComputerConfigDTO.ESDeleteConfigDTO")
    public static class ESDeleteConfigDTO{
        @ApiModelProperty(value = "要删除的id列表", required = true)
        @NotEmpty
        List<Integer> idList;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel("ComputerConfigDTO.ESQueryConfigDTO")
    public static class ESQueryConfigDTO{

        @ApiModelProperty(value = "查询条件", required = true)
        @NotNull
        private JSONObject jsonObject;

        @NotNull
        @Min(1)
        @ApiModelProperty(value = "页码", required = true)
        private Integer page;

        @NotNull
        @Min(1)
        @ApiModelProperty(value = "页面大小", required = true)
        private Integer size;
    }
}
