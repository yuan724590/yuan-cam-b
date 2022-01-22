package yuan.cam.b.configuration;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import yuan.cam.b.commons.LogInterceptor;

@Configuration
public class InterceptorAdapterConfig extends WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter {

    @Autowired
    private LogInterceptor logInterceptor;

    public InterceptorAdapterConfig(ResourceProperties resourceProperties, WebMvcProperties mvcProperties, ListableBeanFactory beanFactory, ObjectProvider<HttpMessageConverters> messageConvertersProvider, ObjectProvider resourceHandlerRegistrationCustomizerProvider) {
        super(resourceProperties, mvcProperties, beanFactory, messageConvertersProvider, resourceHandlerRegistrationCustomizerProvider);
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry){
        //增加拦截器, 增加匹配方式
        registry.addInterceptor(logInterceptor).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}
