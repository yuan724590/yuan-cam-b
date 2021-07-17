package yuan.cam.b.service;

import org.apache.ibatis.annotations.Mapper;
import yuan.cam.b.entity.ComputerConfig;
import yuan.cam.b.vo.ConfigVO;

import java.util.List;
import java.util.Map;

public interface SourceService {

    /**
     * 新增商品信息
     */
    String insertConfig(ComputerConfig computerConfig, String qid);

    /**
     * 删除商品信息
     */
    String deleteConfig(List<Integer> idList, String qid);

    /**
     * 编辑商品信息
     */
    String editConfig(ComputerConfig computerConfig, String qid);

    /**
     * 通用查询商品信息
     */
    List<ConfigVO> queryDetail(Map<String, String> search, Integer page, Integer size, String qid);

    /**
     * 根据商品名称查询商品信息
     */
    List<ConfigVO> queryByName(String goodsName);
}
