package top.rurenyinshui.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @program: lixin
 * @author: LiXin
 * @create: 2019-09-10 17:56
 **/

public class Producer03_routing {

    //队列名称
    private static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    private static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    private static final String EXCHANGE_ROUTING_INFORM="exchange_routing_inform";


    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = null;
        Channel channel = null;
        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("127.0.0.1");
            connectionFactory.setPort(5672);
            connectionFactory.setUsername("guest");
            connectionFactory.setPassword("guest");
            connectionFactory.setVirtualHost("/");

            //创建与RabbitMQ服务的TCP连接
            connection = connectionFactory.newConnection();
            channel = connection.createChannel();
            /**
             * 参数明细
             * 1、交换机名称
             * 2、交换机类型，fanout、topic、direct、headers
             */
            channel.exchangeDeclare(EXCHANGE_ROUTING_INFORM, BuiltinExchangeType.DIRECT);
            /**
             * queue 队列的名称
             * durable 如果我们声明一个持久队列，则为true（队列将在服务器重启后继续存在）
             * exclusive 如果我们声明一个独占队列（仅限于此连接），则为true
             * autoDelete 如果我们声明一个自动删除队列，则为true（服务器将在不再使用时将其删除）
             * arguments 队列的其他属性（构造参数）
             *
             * String queue, boolean durable, boolean exclusive, boolean autoDelete,  Map<String, Object> arguments
             */
            channel.queueDeclare(QUEUE_INFORM_EMAIL, true, false, false, null);
            channel.queueDeclare(QUEUE_INFORM_SMS, true, false, false, null);

            channel.queueBind(QUEUE_INFORM_EMAIL,EXCHANGE_ROUTING_INFORM,QUEUE_INFORM_EMAIL);
            channel.queueBind(QUEUE_INFORM_SMS,EXCHANGE_ROUTING_INFORM,QUEUE_INFORM_SMS);

            for (int i = 0; i < 10; i++) {
                String message = "send email to user :" + i + "," + System.currentTimeMillis();
                /**
                 * 消息发布方法
                 * String exchange, String routingKey, BasicProperties props, byte[] body
                 */
                channel.basicPublish(EXCHANGE_ROUTING_INFORM, QUEUE_INFORM_EMAIL, null, message.getBytes());
                System.out.println("Send email is: " + message);
            }
            for (int i = 0; i < 10; i++) {
                String message = "send sms to user :" + i + "," + System.currentTimeMillis();
                /**
                 * 消息发布方法
                 * String exchange, String routingKey, BasicProperties props, byte[] body
                 */
                channel.basicPublish(EXCHANGE_ROUTING_INFORM, QUEUE_INFORM_SMS, null, message.getBytes());
                System.out.println("Send sms is: " + message);
            }
//            for (int i = 0; i < 10; i++) {
//                String message = "send info to user :" + i + "," + System.currentTimeMillis();
//                /**
//                 * 消息发布方法
//                 * String exchange, String routingKey, BasicProperties props, byte[] body
//                 */
//                channel.basicPublish(EXCHANGE_ROUTING_INFORM, QUEUE_INFORM_EMAIL, null, message.getBytes());
//                System.out.println("Send Message is: " + message);
//            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (channel!=null) {
                channel.close();
            }
            if (connection!=null) {
                connection.close();
            }
        }
    }
}
