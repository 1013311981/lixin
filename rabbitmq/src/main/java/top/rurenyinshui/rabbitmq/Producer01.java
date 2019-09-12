package top.rurenyinshui.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


/**
 * 消息生产者
 *
 * @program: lixin
 * @author: LiXin
 * @create: 2019-09-10 11:40
 **/

public class Producer01 {
    //队列名称
    private static final String QUEUE = "helloworld";

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
             * queue 队列的名称
             * durable 如果我们声明一个持久队列，则为true（队列将在服务器重启后继续存在）
             * exclusive 如果我们声明一个独占队列（仅限于此连接），则为true
             * autoDelete 如果我们声明一个自动删除队列，则为true（服务器将在不再使用时将其删除）
             * arguments 队列的其他属性（构造参数）
             *
             * String queue, boolean durable, boolean exclusive, boolean autoDelete,  Map<String, Object> arguments
             */
            channel.queueDeclare(QUEUE, true, false, false, null);
            String message = "hello LiXin good boy :" + System.currentTimeMillis();
            /**
             * 消息发布方法
             * String exchange, String routingKey, BasicProperties props, byte[] body
             */
            channel.basicPublish("", QUEUE, null, message.getBytes());
            System.out.println("Send Message is: " + message);
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
