package yuan.cam.b.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;
import yuan.cam.b.entity.ComputerConfig;

import java.util.List;


public interface ComputerConfigMapper extends Mapper<ComputerConfig> {

    /**
     * 根据商品id查询商品信息
     */
    @Select("select * from computer_config where id = #{id}")
    ComputerConfig queryById(Integer id);

    /**
     * 根据商品名称查询商品信息
     */
    List<ComputerConfig> queryByName(@Param("goodsName") String goodsName);
}
