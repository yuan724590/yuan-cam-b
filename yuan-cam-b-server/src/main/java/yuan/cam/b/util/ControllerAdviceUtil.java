package yuan.cam.b.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import yuan.cam.b.ContentConst;
import yuan.cam.b.vo.ResultVO;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RestControllerAdvice(basePackages = "yuan.cam.b")
public class ControllerAdviceUtil implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        //拦截返回值范围
        if (ContentConst.NON_INTERCEPT_NAME.containsKey(methodParameter.getExecutable().getName())) {
            return false;
        }
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object object, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (object == null) {
            return new ResultVO(0, "success", null);
        } else if (object instanceof ResultVO) {
            return (ResultVO) object;
        } else if (object instanceof String) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("statusCode", 0);
            jsonObject.put("statusMsg", "success");
            jsonObject.put("result", object.toString());
            return jsonObject.toJSONString();
        } else {
            return new ResultVO(0, "success", object);
        }
    }
}
