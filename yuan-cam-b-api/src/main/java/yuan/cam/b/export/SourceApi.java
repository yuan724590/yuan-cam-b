package yuan.cam.b.export;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import yuan.cam.b.common.Constants;
import yuan.cam.b.dto.ComputerConfigDTO;
import yuan.cam.b.vo.ConfigVO;

import java.util.List;


@Api(value = "数据层变更")
@FeignClient(value = Constants.SERVICE_NAME)
public interface SourceApi {

    @ApiOperation(value = "新增商品信息", response = String.class)
    @PostMapping(value = "/insert", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String insertConfig(@RequestBody @Validated ComputerConfigDTO.InsertConfigDTO reqDTO);

    @ApiOperation(value = "删除商品信息", response = String.class)
    @PostMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String deleteConfig(@RequestBody @Validated ComputerConfigDTO.DeleteConfigDTO reqDTO);

    @ApiOperation(value = "编辑商品信息", response = String.class)
    @PostMapping(value = "/edit", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String editConfig(@RequestBody @Validated ComputerConfigDTO.EditConfigDTO reqDTO);

    @ApiOperation(value = "通用查询商品信息", response = List.class)
    @PostMapping(value = "/query", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    List<ConfigVO> queryDetail(@RequestBody @Validated ComputerConfigDTO.QueryDetailDTO reqDTO);

    @ApiOperation(value = "根据商品名称查询商品信息", response = List.class)
    @PostMapping(value = "/queryByName", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    List<ConfigVO> queryByName(@RequestBody @Validated ComputerConfigDTO.QueryByNameDTO reqDTO);
}
