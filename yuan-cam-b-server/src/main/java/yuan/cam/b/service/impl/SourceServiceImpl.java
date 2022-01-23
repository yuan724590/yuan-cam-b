package yuan.cam.b.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import yuan.cam.b.commons.Constants;
import yuan.cam.b.dto.GoodsInfoDTO;
import yuan.cam.b.service.SourceService;
import yuan.cam.b.entity.ComputerConfig;
import yuan.cam.b.mapper.ComputerConfigMapper;
import yuan.cam.b.util.CommonUtil;
import yuan.cam.b.util.CopierUtil;
import yuan.cam.b.vo.ComputerConfigVO;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SourceServiceImpl implements SourceService {

    @Resource
    private ComputerConfigMapper computerConfigMapper;

    @Override
    public String insertGoods(GoodsInfoDTO dto) {
        ComputerConfig computerConfig = CopierUtil.copyProperties(dto, ComputerConfig.class);
        computerConfig.setUpdateTime(CommonUtil.currentSeconds());
        if(dto.getId() != null && dto.getId() > 0){
            //如果传了id, 认为是已存在的数据, 进行更新
            computerConfigMapper.updateByPrimaryKeySelective(computerConfig);
        }else{
            //如果没传id, 认为是不存在的数据, 进行新增
            computerConfig.setCreateTime(CommonUtil.currentSeconds());
            computerConfigMapper.insertSelective(computerConfig);
        }
        return "成功";
    }

    @Override
    public String deleteGoods(List<Integer> idList) {
        Example example = new Example(ComputerConfig.class);
        example.createCriteria().andIn("id", idList);
        ComputerConfig computerConfig = new ComputerConfig();
        computerConfig.setDeleted(Constants.DELETE_STATUS);
        computerConfigMapper.updateByExampleSelective(computerConfig, example);
        return "删除成功";
    }

    @Override
    public List<ComputerConfigVO> queryDetail(Map<String, String> search, Integer page, Integer size) {
        Example example = new Example(ComputerConfig.class);
        if (search != null && search.size() > 0) {
            Example.Criteria criteria = example.createCriteria();
            for (String key : search.keySet()) {
                if ("goodsName".equals(key)) {
                    criteria.andLike(key, search.get(key));
                } else {
                    criteria.andEqualTo(key, search.get(key));
                }
            }
        }
        int offset = (page - 1) * size;
        List<ComputerConfig> configList = computerConfigMapper.selectByExampleAndRowBounds(example, new RowBounds(offset, size));
        return CopierUtil.copyobjects(configList, ComputerConfigVO.class);
    }

    @Override
    public List<ComputerConfigVO> queryByName(String goodsName){
        List<ComputerConfig> list = computerConfigMapper.queryByName(goodsName);
        return CopierUtil.copyobjects(list, ComputerConfigVO.class);
    }
}
