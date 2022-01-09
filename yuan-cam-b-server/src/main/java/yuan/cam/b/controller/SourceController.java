package yuan.cam.b.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yuan.cam.b.dto.ComputerConfigDTO;
import yuan.cam.b.entity.ComputerConfig;
import yuan.cam.b.export.SourceApi;
import yuan.cam.b.service.SourceService;
import yuan.cam.b.vo.ConfigVO;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@RequestMapping
@RestController
public class SourceController implements SourceApi {
    @Resource
    private SourceService sourceService;

    @Override
    public String insertConfig(@RequestBody @Validated ComputerConfigDTO.InsertConfigDTO reqDTO) {
        String qid = UUID.randomUUID().toString();
        ComputerConfig computerConfig = new ComputerConfig();
        computerConfig.setBrand(reqDTO.getBrand());
        computerConfig.setFloorPrice(reqDTO.getFloorPrice());
        computerConfig.setType(reqDTO.getType());
        computerConfig.setGoodsName(reqDTO.getGoodsName());
        computerConfig.setPrice(reqDTO.getPrice());
        computerConfig.setUpdateTime((int) (System.currentTimeMillis() / 1000));
        computerConfig.setCreateTime((int) (System.currentTimeMillis() / 1000));
        return sourceService.insertConfig(computerConfig, qid);
    }

    @Override
    public String deleteConfig(@RequestBody @Validated ComputerConfigDTO.DeleteConfigDTO reqDTO) {
        String qid = UUID.randomUUID().toString();
        return sourceService.deleteConfig(reqDTO.getIdList(), qid);
    }

    @Override
    public String editConfig(@RequestBody @Validated ComputerConfigDTO.EditConfigDTO reqDTO) {
        String qid = UUID.randomUUID().toString();
        ComputerConfig computerConfig = new ComputerConfig();
        computerConfig.setId(reqDTO.getId());
        computerConfig.setBrand(reqDTO.getBrand());
        computerConfig.setFloorPrice(reqDTO.getFloorPrice());
        computerConfig.setType(reqDTO.getType());
        computerConfig.setGoodsName(reqDTO.getGoodsName());
        computerConfig.setPrice(reqDTO.getPrice());
        return sourceService.editConfig(computerConfig, qid);
    }

    @Override
    public List<ConfigVO> queryDetail(@RequestBody @Validated ComputerConfigDTO.QueryDetailDTO reqDTO) {
        String qid = UUID.randomUUID().toString();
        return sourceService.queryDetail(reqDTO.getSearch(), reqDTO.getPage(), reqDTO.getSize(), qid);
    }

    @Override
    public List<ConfigVO> queryByName(@RequestBody @Validated ComputerConfigDTO.QueryByNameDTO reqDTO){
        return sourceService.queryByName(reqDTO.getGoodsName());
    }
}
