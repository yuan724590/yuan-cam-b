package yuan.cam.b.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import yuan.cam.b.service.SourceService;
import yuan.cam.b.entity.ComputerConfig;
import yuan.cam.b.mapper.ComputerConfigMapper;
import yuan.cam.b.util.CommonUtil;
import yuan.cam.b.util.LogUtil;
import yuan.cam.b.vo.ConfigVO;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SourceServiceImpl implements SourceService {

    @Resource
    private ComputerConfigMapper computerConfigMapper;

    @Override
    public String insertConfig(ComputerConfig computerConfig, String qid) {
        if (computerConfig == null) {
            return "false";
        }
        LogUtil.debug("开始进行新增", qid);
        computerConfigMapper.insert(computerConfig);
        return "true";
    }

    @Override
    public String deleteConfig(List<Integer> idList, String qid) {
        LogUtil.debug("开始进行删除", qid);
        Example example = new Example(ComputerConfig.class);
        example.createCriteria().andIn("id", idList);
        computerConfigMapper.deleteByExample(example);
        return "true";
    }

    @Override
    public String editConfig(ComputerConfig computerConfig, String qid) {
        LogUtil.debug("开始进行编辑", qid);
        computerConfigMapper.updateByPrimaryKeySelective(computerConfig);
        return "true";
    }

    @Override
    public List<ConfigVO> queryDetail(Map<String, String> search, Integer page, Integer size, String qid) {
        LogUtil.debug("开始进行查询", qid);
        Example example = new Example(ComputerConfig.class);
        if (search != null && search.size() > 0) {
            Example.Criteria criteria = example.createCriteria();
            for (String key : search.keySet()) {
                if (key.equals("name")) {
                    criteria.andLike(key, search.get(key));
                } else {
                    criteria.andEqualTo(key, search.get(key));
                }
            }
        }
        int offset = (page - 1) * size;
        List<ComputerConfig> configList = computerConfigMapper.selectByExampleAndRowBounds(example, new RowBounds(offset, size));
        return configList.stream().map(element -> {
            ConfigVO configVO = new ConfigVO();
            configVO.setId(element.getId());
            configVO.setBrand(element.getBrand());
            configVO.setType(element.getType());
            configVO.setGoodsName(element.getGoodsName());
            configVO.setPrice(element.getPrice());
            configVO.setFloorPrice(element.getFloorPrice());
            return configVO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ConfigVO> queryByName(String goodsName){
//        ComputerConfig list = computerConfigMapper.queryById(1);
//        return null;
        List<ComputerConfig> list = computerConfigMapper.queryByName(goodsName);
        return CommonUtil.batchCopyProperties(list, ConfigVO.class);
    }
}
