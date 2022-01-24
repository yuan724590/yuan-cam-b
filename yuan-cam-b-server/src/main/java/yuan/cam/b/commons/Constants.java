package yuan.cam.b.commons;

import com.alibaba.fastjson.JSONObject;

public class Constants {

    public final static String SERVICE_NAME_LOWERCASE = "yuan-cam-b";

    public final static String SERVICE_NAME = "YUAN-CAM-B";

    public final static String COMPUTER_CONFIG_INDEX = "computer_config";

    public final static String ES_LOG = "computer_config_log";

    public final static String MQ = "computer-config";

    public final static String COMPUTER_CONFIG = "computer_config";

    public final static JSONObject NON_INTERCEPT_NAME = JSONObject.parseObject("{\"uiConfiguration\":1,\"securityConfiguration\":1,\"swaggerResources\":1,\"getDocumentation\":1}");

    /**
     * 请求成功的状态码
     */
    public final static Integer SUCCESS_CODE = 0;

    /**
     * 删除状态
     */
    public final static Byte DELETE_STATUS = 1;

    /**
     * 未删除状态
     */
    public final static Byte NOT_DELETE_STATUS = 0;

    public final static String SUCCESS = "success";
    public final static String FAIL = "fail";
    public final static String _ID = "_id";
    public final static String ID = "id";
    public final static String GOODS_NAME = "goodsName";
    public final static String UTF_8 = "UTF-8";
}
