package yuan.cam.b.mapper;

import feign.Param;
import tk.mybatis.mapper.common.Mapper;
import yuan.cam.b.entity.ComputerConfig;

public interface ComputerConfigMapper extends Mapper<ComputerConfig> {

    ComputerConfig queryById(@Param("id") Integer id);
}
