package yuan.cam.bb.service.impl;



import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import redis.clients.jedis.Jedis;
import yuan.cam.bb.service.HelloService;
import yuan.cam.bb.util.LogUtil;
import yuan.cam.bb.util.RabbitConsumer;
import yuan.cam.bb.util.RabbitProducer;

import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class HelloServiceImpl implements HelloService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public String sayHello(String name) {
        boolean lock = redisTemplate.opsForValue().setIfAbsent(name, "lock", 5*1000, TimeUnit.MILLISECONDS);
        if (lock) {
            System.out.println("执行任务");
        } else {
            return "没有抢到锁";
        }

        if(redisTemplate.boundValueOps("name").get() == null){
            redisTemplate.boundValueOps("name").set(name);
            redisTemplate.expire("name", 5, TimeUnit.SECONDS);
        }
        return "返回" + name + "_" + redisTemplate.boundValueOps("name").get();

//方案2
//        Jedis jedis = null;
//        try{
//            jedis = RedisUtil.getJedis(qid);
//            if(jedis.get(name) == null){
//                jedis.set(name, "1");
//            }else{
//                jedis.set(name, (Integer.parseInt(jedis.get(name)) + 1) + "");
//            }
//            LogUtil.error(name, qid);
//            return "返回" + name + jedis.get(name);
//        }finally {
//            if(jedis != null){
//                jedis.close();
//            }
//        }
    }

    @Override
    public String sayHelloAgain() {
        String qid = UUID.randomUUID().toString();
        System.out.println(111);
        LogUtil.error("打印error日志", qid);
        LogUtil.warn("打印warn日志", qid);
        LogUtil.info("打印info日志", qid);
        LogUtil.debug("打印debug日志", qid);
        return "ok";
    }

    @Override
    public String sayHelloHystrix() {
        return "ok";
    }

    @Override
    public String helloA(Object object) {
        return object.toString();
    }

    @Override

    public String rest(String msg) {
        return "{msg:" + msg + "}";
    }

    @Autowired
    private RabbitProducer rabbitProducer;

//    @Autowired
//    private RabbitConsumer rabbitConsumer;

    @Override
    public void helloFanout(String message) {
        try {
            rabbitProducer.fanoutSend(message);
            //先开启消费者，这样生产者不会因为没有消费者而丢掉数据
//            rabbitConsumer.fanoutExchange();
            //开启生产者
//            rabbitProducer.fanoutExchange(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void helloDirect(String message) {
        try {
            rabbitProducer.directSend(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void helloTopic(String message) {
        try {
            rabbitProducer.topicSend(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
