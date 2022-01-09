package yuan.cam.b.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import yuan.cam.b.service.HelloService;
import yuan.cam.b.util.LogUtil;
import yuan.cam.b.util.RabbitProducer;

import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class HelloServiceImpl implements HelloService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public String sayHello(String name) {
        boolean lock = redisTemplate.opsForValue().setIfAbsent(name, "lock", 5 * 1000, TimeUnit.MILLISECONDS);
        if (lock) {
            System.out.println("执行任务");
        } else {
            return "没有抢到锁";
        }

        if (redisTemplate.boundValueOps("name").get() == null) {
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
        long t1 = System.currentTimeMillis();
        String qid = UUID.randomUUID().toString();
        System.out.println(111);
        long t2 = System.currentTimeMillis();

//        LogUtil.error("打印error日志", qid);
//        LogUtil.warn("打印warn日志", qid);
//        LogUtil.info("打印info日志", qid);
//        LogUtil.debug("打印debug日志", qid);
        long t3 = System.currentTimeMillis();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void helloDirect(String message) {
        try {
            rabbitProducer.directSend(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void helloTopic(String message) {
        try {
            rabbitProducer.topicSend(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
