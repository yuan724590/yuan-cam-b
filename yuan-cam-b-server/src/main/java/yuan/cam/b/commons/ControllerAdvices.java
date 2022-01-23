package yuan.cam.b.commons;

import com.alibaba.fastjson.JSONObject;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import yuan.cam.b.vo.ResultVO;

@ControllerAdvice(basePackageClasses = {yuan.cam.b.export.SourceApi.class})
//@RestControllerAdvice(basePackageClasses = {yuan.cam.b.export.SourceApi.class})
public class ControllerAdvices implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        //拦截返回值范围
        return !Constants.NON_INTERCEPT_NAME.containsKey(methodParameter.getExecutable().getName());
    }

    @Override
    public Object beforeBodyWrite(Object object, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        JSONObject jsonObject = (JSONObject) object;
        String statusMsg = Constants.SUCCESS_CODE.equals(jsonObject.getIntValue("status")) ? Constants.SUCCESS : Constants.FAIL;
        return new ResultVO<>(jsonObject.getIntValue("status"), statusMsg, JSONObject.toJSONString(object));
    }
}
