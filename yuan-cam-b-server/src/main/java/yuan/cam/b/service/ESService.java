package yuan.cam.b.service;

import yuan.cam.b.dto.EsDTO;
import yuan.cam.b.dto.GoodsInfoDTO;
import yuan.cam.b.vo.BrandCountVO;
import yuan.cam.b.vo.ComputerConfigVO;
import yuan.cam.b.vo.Page;

import java.util.List;

public interface ESService {

    /**
     * 新增商品
     */
    String insertGoods(GoodsInfoDTO dto);

    /**
     * 删除商品
     */
    String deleteGoods(List<Integer> idList);

    /**
     * 查询商品信息
     */
    Page<List<ComputerConfigVO>> queryGoods(EsDTO.ESQueryGoodsDTO dto);

    /**
     * 深分页查询商品信息
     */
    List<ComputerConfigVO> queryByScroll();

    /**
     * 第一次-通过游标进行查询
     */
    String firstQueryEsByScroll(List<ComputerConfigVO> list, int size);

    /**
     * 后续-通过游标进行查询
     */
    String queryEsByScroll(List<ComputerConfigVO> list, String scrollId);

    /**
     * 通过商品id查询是否存在
     */
    Boolean queryIsExistById(Integer id);

    /**
     * 查询每个品牌未删除的商品个数
     */
    BrandCountVO queryGoodsCount();

    /**
     * 随机获取商品
     */
    Page<List<ComputerConfigVO>> randomAcquisitionGoods(Integer page, Integer size);
}
