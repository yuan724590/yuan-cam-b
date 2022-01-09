package yuan.cam.b.export;


import com.alibaba.fastjson.JSONArray;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import yuan.cam.b.common.Constants;
import yuan.cam.b.dto.ESDTO;
import yuan.cam.b.vo.ComputerConfigVO;

import java.util.List;


@Api(value = "ES变更")
@FeignClient(value = Constants.SERVICE_NAME)
public interface ESApi {

    @ApiOperation(value = "新增商品信息", response = String.class)
    @PostMapping(value = "/es/insert", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String insertConfig(@RequestBody @Validated ESDTO.ESInsertConfigDTO reqDTO);

    @ApiOperation(value = "删除商品信息", response = String.class)
    @PostMapping(value = "/es/delete", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String deleteConfig(@RequestBody @Validated ESDTO.ESDeleteConfigDTO reqDTO);

    @ApiOperation(value = "查询商品信息", response = JSONArray.class)
    @PostMapping(value = "/es/query", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    List<ComputerConfigVO> queryConfig(@RequestBody @Validated ESDTO.ESQueryConfigDTO reqDTO);

}
