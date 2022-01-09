package yuan.cam.b.commons;

import com.alibaba.fastjson.JSONObject;

public class Constants {

    public final static String SERVICE_NAME_LOWERCASE = "yuan-cam-b";

    public final static String SERVICE_NAME = "YUAN-CAM-B";

    public final static String ES_INDEX = "computer_config";

    public final static String ES_LOG = "computer_config_log";

    public final static String MQ = "computer-config";

    public final static String COMPUTER_CONFIG = "computer_config";

    public final static JSONObject NON_INTERCEPT_NAME = JSONObject.parseObject("{\"uiConfiguration\":1,\"securityConfiguration\":1,\"swaggerResources\":1,\"error\":1,\"getDocumentation\":1}");
}
