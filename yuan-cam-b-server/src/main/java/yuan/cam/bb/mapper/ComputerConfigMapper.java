package yuan.cam.bb.mapper;

import feign.Param;
import tk.mybatis.mapper.common.BaseMapper;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import yuan.cam.bb.entity.ComputerConfig;

public interface ComputerConfigMapper extends Mapper<ComputerConfig> {

    ComputerConfig queryById(@Param("id") Integer id);
}
