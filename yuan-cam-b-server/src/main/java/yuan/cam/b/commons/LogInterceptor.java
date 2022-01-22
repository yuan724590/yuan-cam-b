package yuan.cam.b.commons;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.NamedThreadLocal;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

@Slf4j
@Component
public class LogInterceptor extends HandlerInterceptorAdapter {

    private NamedThreadLocal<Long>  startTimeThreadLocal = new NamedThreadLocal<>("StopWatch-StartTime");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        startTimeThreadLocal.set(System.currentTimeMillis());
        log.debug("进入到拦截器了, url:{}, header:{}, param:{}", getPath(request), getHeader(request), JSONObject.toJSONString(request.getParameterMap()));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex){
        long time = System.currentTimeMillis() - startTimeThreadLocal.get();
        log.debug("又到拦截器了, url:{}, 请求耗时为:{}ms", getPath(request), time);
    }

    /**
     * 获取请求的头
     */
    private String getHeader(HttpServletRequest request){
        Enumeration<String> enumeration = request.getHeaderNames();
        StringBuilder stringBuilder = new StringBuilder();
        String header;
        while(enumeration.hasMoreElements()){
            header = enumeration.nextElement();
            stringBuilder.append(",").append(header).append("=").append(request.getHeader(header));
        }
        return stringBuilder.length() > 0 ? stringBuilder.substring(1) : "";
    }

    /**
     * 获取请求的地址
     */
    private String getPath(HttpServletRequest request){
        return StringUtils.isNotBlank(request.getServletPath()) ? request.getServletPath() : request.getRequestURI();
    }
}
