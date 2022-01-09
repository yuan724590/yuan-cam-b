package yuan.cam.b.commons;

import com.alibaba.fastjson.JSONObject;

public interface Constants {
    String SERVICE_NAME_LOWERCASE = "yuan-cam-b";

    String SERVICE_NAME = "YUAN-CAM-B";

    String ES_INDEX = "computer_config";

    String ES_LOG = "computer_config_log";

    String MQ = "computer-config";

    String COMPUTER_CONFIG = "computer_config";

    JSONObject NON_INTERCEPT_NAME = JSONObject.parseObject("{\"uiConfiguration\":1,\"securityConfiguration\":1,\"swaggerResources\":1,\"error\":1,\"getDocumentation\":1}");
}
