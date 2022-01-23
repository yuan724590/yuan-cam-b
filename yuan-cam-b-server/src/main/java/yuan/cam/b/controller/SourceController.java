package yuan.cam.b.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yuan.cam.b.dto.ComputerConfigDTO;
import yuan.cam.b.dto.GoodsInfoDTO;
import yuan.cam.b.export.SourceApi;
import yuan.cam.b.service.SourceService;
import yuan.cam.b.util.ResultUtils;
import yuan.cam.b.vo.ComputerConfigVO;
import yuan.cam.b.vo.ResultVO;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping
@RestController
public class SourceController implements SourceApi {

    @Resource
    private SourceService sourceService;

    @Override
    public ResultVO<String> insertGoods(@RequestBody @Validated GoodsInfoDTO dto) {
        return ResultUtils.data(sourceService.insertGoods(dto));
    }

    @Override
    public ResultVO<String> deleteGoods(@RequestBody @Validated ComputerConfigDTO.DeleteConfigDTO dto) {
        return ResultUtils.data(sourceService.deleteGoods(dto.getIdList()));
    }

    @Override
    public List<ComputerConfigVO> queryDetail(@RequestBody @Validated ComputerConfigDTO.QueryDetailDTO dto) {
        return sourceService.queryDetail(dto.getSearch(), dto.getPage(), dto.getSize());
    }

    @Override
    public List<ComputerConfigVO> queryByName(@RequestBody @Validated ComputerConfigDTO.QueryByNameDTO dto){
        return sourceService.queryByName(dto.getGoodsName());
    }
}
