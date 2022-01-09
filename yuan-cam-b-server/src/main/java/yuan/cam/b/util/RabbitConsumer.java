package yuan.cam.b.util;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitConsumer {

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "mq_fanoutExchange", autoDelete = "true"),
            exchange = @Exchange(value = "fanoutExchange", type = ExchangeTypes.FANOUT)))
    public void fanoutExchange(Message msg) {
        System.out.println(msg);
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "mq_directExchange", autoDelete = "true"),
            exchange = @Exchange(value = "directExchange", type = ExchangeTypes.DIRECT), key = "routingkey"))
    public void directExchange(Message msg) {
        System.out.println(msg);
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "mq_topicExchange", autoDelete = "true"),
            exchange = @Exchange(value = "topicExchange", type = ExchangeTypes.TOPIC), key = "routing.#"))
    public void topicExchange(Message msg) {
        System.out.println(msg);
    }

    /*public void fanoutExchange(){
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            //交换机声明（参数为：交换机名称；交换机类型）
            channel.exchangeDeclare("fanoutLogs",BuiltinExchangeType.FANOUT);
            //获取一个临时队列
            String queueName = channel.queueDeclare().getQueue();
            //队列与交换机绑定（参数为：队列名称；交换机名称；routingKey忽略）
            channel.queueBind(queueName,"fanoutLogs","");


            //这里重写了DefaultConsumer的handleDelivery方法，因为发送的时候对消息进行了getByte()，在这里要重新组装成String
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    super.handleDelivery(consumerTag, envelope, properties, body);
                    String message = new String(body,"UTF-8");
                    System.out.println("received:" + message);
                }
            };
            //声明队列中被消费掉的消息（参数为：队列名称；消息是否自动确认;consumer主体）
            channel.basicConsume(queueName,true,consumer);
            //这里不能关闭连接，调用了消费方法后，消费者会一直连接着rabbitMQ等待消费
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
