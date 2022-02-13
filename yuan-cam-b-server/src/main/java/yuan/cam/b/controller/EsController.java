package yuan.cam.b.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yuan.cam.b.dto.EsDTO;
import yuan.cam.b.dto.GoodsInfoDTO;
import yuan.cam.b.export.EsApi;
import yuan.cam.b.service.ESService;
import yuan.cam.b.util.ResultUtils;
import yuan.cam.b.vo.BrandCountVO;
import yuan.cam.b.vo.ComputerConfigVO;
import yuan.cam.b.vo.Page;
import yuan.cam.b.vo.ResultVO;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping
@RestController
public class EsController implements EsApi {

    @Resource
    private ESService esService;

    @Override
    public ResultVO<String> insertGoods(@RequestBody @Validated GoodsInfoDTO dto) {
        return ResultUtils.data(esService.insertGoods(dto));
    }

    @Override
    public ResultVO<String> deleteGoods(@RequestBody @Validated EsDTO.ESDeleteGoodsDTO dto) {
        return ResultUtils.data(esService.deleteGoods(dto.getIdList()));
    }

    @Override
    public ResultVO<Page<List<ComputerConfigVO>>> queryGoods(@RequestBody @Validated EsDTO.ESQueryGoodsDTO dto){
        return ResultUtils.data(esService.queryGoods(dto));
    }

    @Override
    public ResultVO<List<ComputerConfigVO>> queryByScroll(){
        return ResultUtils.data(esService.queryByScroll());
    }

    @Override
    public ResultVO<Boolean> queryIsExistById(@RequestBody @Validated EsDTO.QueryIsExistByIdDTO dto){
        return ResultUtils.data(esService.queryIsExistById(dto.getId()));
    }

    @Override
    public ResultVO<BrandCountVO> queryGoodsCount(){
        return ResultUtils.data(esService.queryGoodsCount());
    }

    @Override
    public ResultVO<Page<List<ComputerConfigVO>>> randomAcquisitionGoods(@RequestBody @Validated EsDTO.RandomAcquisitionGoodsDTO dto){
        return ResultUtils.data(esService.randomAcquisitionGoods(dto.getPage(), dto.getSize()));
    }
}
