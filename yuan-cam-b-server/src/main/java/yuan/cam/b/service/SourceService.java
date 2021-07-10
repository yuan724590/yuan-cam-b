package yuan.cam.b.service;

import yuan.cam.b.entity.ComputerConfig;
import yuan.cam.b.vo.ConfigVO;

import java.util.List;
import java.util.Map;

public interface SourceService {
    String insertConfig(ComputerConfig computerConfig, String qid);

    String deleteConfig(List<Integer> idList, String qid);

    String editConfig(ComputerConfig computerConfig, String qid);

    List<ConfigVO> queryConfig(Map<String, String> search, Integer page, Integer size, String qid);
}
