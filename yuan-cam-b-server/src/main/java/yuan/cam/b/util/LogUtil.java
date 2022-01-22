package yuan.cam.b.util;

import com.alibaba.fastjson.JSONObject;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import java.util.Set;
import java.util.UUID;

import static yuan.cam.b.commons.Constants.ES_LOG;
import static yuan.cam.b.commons.Constants.SERVICE_NAME;

@Slf4j
@Configuration
public class LogUtil {

    /**
     * 监听关键字，当配置中心的依次开头的配置发生变化时，日志级别刷新
     */
    @Value("${log.tag}")
    private String LOGGER_TAG;

    @Resource
    private LoggingSystem loggingSystem;

    /**
     * 可以指定具体的namespace，未指定时使用的是 application这个namespace
     */
    @ApolloConfig
    private Config config;

    @ApolloConfigChangeListener
    private void onChange(ConfigChangeEvent changeEvent) {
        refreshLoggingLevels();
    }

    @PostConstruct
    private void refreshLoggingLevels() {
        Set<String> keyNames = config.getPropertyNames();
        for (String key : keyNames) {
            if (key.equals(LOGGER_TAG)) {
                String strLevel = config.getProperty(key, "debug");
                LogLevel level = LogLevel.valueOf(strLevel.toUpperCase());
                //重置日志级别，马上生效
                loggingSystem.setLogLevel("", level);
            }
        }
    }
}
