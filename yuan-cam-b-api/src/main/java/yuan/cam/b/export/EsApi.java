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
import yuan.cam.b.dto.EsDTO;
import yuan.cam.b.dto.GoodsInfoDTO;
import yuan.cam.b.vo.BrandCountVO;
import yuan.cam.b.vo.ComputerConfigVO;
import yuan.cam.b.vo.Page;
import yuan.cam.b.vo.ResultVO;

import java.util.List;


@Api("ES使用实例")
@FeignClient(value = Constants.SERVICE_NAME)
public interface EsApi {

    @ApiOperation(value = "新增商品信息", response = String.class)
    @PostMapping(value = "/es/insert/goods", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResultVO<String> insertGoods(@RequestBody @Validated GoodsInfoDTO dto);

    @ApiOperation(value = "删除商品信息", response = String.class)
    @PostMapping(value = "/es/del/goods", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResultVO<String> deleteGoods(@RequestBody @Validated EsDTO.ESDeleteGoodsDTO dto);

    @ApiOperation(value = "查询商品信息", response = ComputerConfigVO.class)
    @PostMapping(value = "/es/query/goods", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResultVO<Page<List<ComputerConfigVO>>> queryGoods(@RequestBody @Validated EsDTO.ESQueryGoodsDTO dto);

    @ApiOperation(value = "深分页查询商品信息", response = ComputerConfigVO.class)
    @PostMapping(value = "/es/queryByScroll/goods", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResultVO<List<ComputerConfigVO>> queryByScroll();

    @ApiOperation(value = "通过商品id查询是否存在", response = Boolean.class)
    @PostMapping(value = "/es/query/isExistById", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResultVO<Boolean> queryIsExistById(@RequestBody @Validated EsDTO.QueryIsExistByIdDTO dto);

    @ApiOperation(value = "查询每个品牌未删除的商品个数", response = Boolean.class)
    @PostMapping(value = "/es/query/goodsCount", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResultVO<BrandCountVO> queryGoodsCount();
}
