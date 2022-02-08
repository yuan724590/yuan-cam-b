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
import yuan.cam.b.dto.GoodsInfoDTO;
import yuan.cam.b.vo.ComputerConfigVO;
import yuan.cam.b.vo.ResultVO;

import java.util.List;


@Api(value = "数据层变更")
@FeignClient(value = Constants.SERVICE_NAME)
public interface SourceApi {

    @ApiOperation(value = "新增商品信息", response = String.class)
    @PostMapping(value = "/insert/goods", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResultVO<String> insertGoods(@RequestBody @Validated GoodsInfoDTO dto);

    @ApiOperation(value = "删除商品信息", response = String.class)
    @PostMapping(value = "/del/goods", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResultVO<String> deleteGoods(@RequestBody @Validated ComputerConfigDTO.DeleteConfigDTO dto);

    @ApiOperation(value = "通用查询商品信息", response = ComputerConfigVO.class)
    @PostMapping(value = "/query/goods", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResultVO<List<ComputerConfigVO>> queryDetail(@RequestBody @Validated ComputerConfigDTO.QueryDetailDTO dto);

    @ApiOperation(value = "根据商品名称查询商品信息", response = ComputerConfigVO.class)
    @PostMapping(value = "/queryByName", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResultVO<List<ComputerConfigVO>> queryByName(@RequestBody @Validated ComputerConfigDTO.QueryByNameDTO dto);

    @ApiOperation(value = "随机获取商品", response = ComputerConfigVO.class)
    @PostMapping(value = "/random/acquisition/goods", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResultVO<List<ComputerConfigVO>> randomAcquisitionGoods();
}
