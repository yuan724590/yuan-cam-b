package yuan.cam.bb.util;

import com.rabbitmq.client.*;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import yuan.cam.bb.ContentConst;
import javax.annotation.Resource;
import java.io.IOException;

@Component
public class RabbitProducer {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /*
     * 发送fanout消息的方法
     */
    public void fanoutSend(String msg){
        /**
         * convertAndSend - 转换并发送消息的template方法。
         * 是将传入的普通java对象，转换为rabbitmq中需要的message类型对象，并发送消息到rabbitmq中。
         * 参数一：交换器名称。 类型是String
         * 参数二：路由键。 类型是String
         * 参数三：消息，是要发送的消息内容对象。类型是String
         */
        rabbitTemplate.convertAndSend("fanoutExchange", "", msg);
    }

    /*
     * 发送direct消息的方法
     */
    public void directSend(String msg){
        /**
         * convertAndSend - 转换并发送消息的template方法。
         * 是将传入的普通java对象，转换为rabbitmq中需要的message类型对象，并发送消息到rabbitmq中。
         * 参数一：交换器名称。 类型是String
         * 参数二：路由键。 类型是String
         * 参数三：消息，是要发送的消息内容对象。类型是String
         */
        rabbitTemplate.convertAndSend("directExchange", "routingkey", msg);
    }

    /*
     * 发送topic消息的方法
     */
    public void topicSend(String msg){
        /**
         * convertAndSend - 转换并发送消息的template方法。
         * 是将传入的普通java对象，转换为rabbitmq中需要的message类型对象，并发送消息到rabbitmq中。
         * 参数一：交换器名称。 类型是String
         * 参数二：路由键。 类型是String
         * 参数三：消息，是要发送的消息内容对象。类型是String
         */
        rabbitTemplate.convertAndSend("topicExchange", "routing.key", msg);
    }

    public static void main(String[] args) {
    }

    public void fanoutExchange(String message){
        //New一个RabbitMQ的连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置需要连接的RabbitMQ地址，这里指向本机
        factory.setHost("localhost");
        try {
            //尝试获取一个连接
            Connection connection = factory.newConnection();
            //尝试创建一个channel
            Channel channel = connection.createChannel();
            //声明交换机（参数为：交换机名称; 交换机类型，广播模式）
            channel.exchangeDeclare("fanoutLogs", BuiltinExchangeType.FANOUT);
            //消息发布（参数为：交换机名称; routingKey，忽略。在广播模式中，生产者声明交换机的名称和类型即可）
            channel.basicPublish("fanoutLogs","", null,message.getBytes());
            channel.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void directExchange(String message){
        //New一个RabbitMQ的连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置需要连接的RabbitMQ地址，这里指向本机
        factory.setHost("localhost");
        try {
            //尝试获取一个连接
            Connection connection = factory.newConnection();
            //尝试创建一个channel
            Channel channel = connection.createChannel();
            channel.queueDeclare("directExchange", true, false, false, null);
            channel.queueBind("mq_directExchange", "directExchange", "mq_directExchange");
            // 往转发器上发送消息
            channel.basicPublish("directExchange", "mq_directExchange", null, message.getBytes());
            System.out.println("[Sent]'" + message + "'");
            channel.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
