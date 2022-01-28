package yuan.cam.b.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Throwables;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.Acknowledgment;
import yuan.cam.b.commons.Constants;
import yuan.cam.b.commons.EsCommonService;

import javax.annotation.Resource;


@Slf4j
@Configuration
public class KafkaConsumer {

    @Resource
    private EsCommonService esCommonService;

    @KafkaListener(topics = "example", id = "consumer", containerFactory = "kafkaListenerContainerFactory")
    public void listen(ConsumerRecord<?, ?> record, Acknowledgment acknowledgment) {
        try{
            log.info("kafka消费者收到消息:{}", JSONObject.toJSONString(record.value()));
            JSONObject jsonObject = JSON.parseObject(record.value().toString());
            if(!"computer".equals(jsonObject.getString("database")) || !"computer_config".equals(jsonObject.getString("table"))){
                //其他情况暂不处理, 也可以通过canal配置修改
                //进行ack回复
                acknowledgment.acknowledge();
                return;
            }
            //将数据库变更同步给es
            esCommonService.insert(Constants.COMPUTER_CONFIG_INDEX, jsonObject.getJSONArray("data").get(0));
            //进行ack回复
            acknowledgment.acknowledge();
        }catch (Exception e){
            log.error("kafka消费异常, 入参:{}:{}, e:{}",
                    JSONObject.toJSONString(record.key()), JSONObject.toJSONString(record.value()), Throwables.getStackTraceAsString(e));
        }
    }
}