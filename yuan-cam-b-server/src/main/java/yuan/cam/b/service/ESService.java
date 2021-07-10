package yuan.cam.b.service;

import com.alibaba.fastjson.JSONObject;
import yuan.cam.b.dto.ConfigDTO;
import yuan.cam.b.vo.ComputerConfigVO;

import java.util.List;

public interface ESService {
    String insertConfig(ConfigDTO configDTO, String qid);

    String updateConfig(String _id, double floorPrice, String qid);

    String deleteConfig(List<Integer> idList, String qid);

    List<ComputerConfigVO> queryConfig(JSONObject jsonObject, Integer page, Integer size, String qid);

    Integer queryCount(JSONObject jsonObject, String qid);
}
