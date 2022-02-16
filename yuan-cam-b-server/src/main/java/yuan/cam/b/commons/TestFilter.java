package yuan.cam.b.commons;

import javax.servlet.*;
import org.springframework.stereotype.Component;

import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//也可以使用@Component注解来实现, 这样在启动时也不需要@ServletComponentScan, 但只能全局代理
//@Component
@WebFilter(filterName = "testFilter",urlPatterns = {"/declare"})
public class TestFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        System.out.println("TestFilter,"+request.getRequestURI());

        //执行
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
