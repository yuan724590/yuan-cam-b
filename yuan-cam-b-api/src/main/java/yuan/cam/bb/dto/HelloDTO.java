package yuan.cam.bb.dto;

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
import java.util.Map;

public class HelloDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel("HelloDTO.SayHelloDTO")
    public static class SayHelloDTO{
        @NotNull
        @ApiModelProperty(value = "key", required = true)
        private String name;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel("HelloDTO.HelloADTO")
    public static class HelloADTO{
        @NotNull
        @Min(1)
        @ApiModelProperty(value = "页码", required = true)
        private Integer page;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel("HelloDTO.HelloBDTO")
    public static class HelloBDTO{
        @NotNull
        @ApiModelProperty(value = "字符串", required = true)
        private String str;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel("HelloDTO.HelloFanoutDTO")
    public static class HelloFanoutDTO{
        @NotNull
        @ApiModelProperty(value = "消息内容", required = true)
        private String message;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel("HelloDTO.HelloDirectDTO")
    public static class HelloDirectDTO{
        @NotNull
        @ApiModelProperty(value = "消息内容", required = true)
        private String message;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel("HelloDTO.HelloTopicDTO")
    public static class HelloTopicDTO{
        @NotNull
        @ApiModelProperty(value = "消息内容", required = true)
        private String message;
    }
}
