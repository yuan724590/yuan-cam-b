package yuan.cam.b;

import com.alibaba.fastjson.JSONObject;

public interface ContentConst {
//    String SERVICE_NAME = "yuan-cam-bb";
    String SERVICE_NAME = "YUAN-CAM-BB";

    String ES_INDEX = "computer_config";

    String ES_LOG = "computer_config_log";

    String MQ = "computer-config";

    String COMPUTER_CONFIG = "computer_config";

    JSONObject NON_INTERCEPT_NAME = JSONObject.parseObject("{\"uiConfiguration\":1,\"securityConfiguration\":1,\"swaggerResources\":1,\"error\":1,\"getDocumentation\":1}");
}
