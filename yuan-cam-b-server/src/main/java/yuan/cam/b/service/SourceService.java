package yuan.cam.b.service;

import yuan.cam.b.dto.GoodsInfoDTO;
import yuan.cam.b.vo.ComputerConfigVO;

import java.util.List;
import java.util.Map;

public interface SourceService {

    /**
     * 新增商品信息
     */
    String insertGoods(GoodsInfoDTO dto);

    /**
     * 删除商品信息
     */
    String deleteGoods(List<Integer> idList);

    /**
     * 通用查询商品信息
     */
    List<ComputerConfigVO> queryDetail(Map<String, String> search, Integer page, Integer size);

    /**
     * 根据商品名称查询商品信息
     */
    List<ComputerConfigVO> queryByName(String goodsName);
}
