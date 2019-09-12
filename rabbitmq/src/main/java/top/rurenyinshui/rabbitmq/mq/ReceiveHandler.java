package top.rurenyinshui.rabbitmq.mq;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import top.rurenyinshui.rabbitmq.config.RabbitmqConfig;

/**
 * @program: lixin
 * @author: LiXin
 * @create: 2019-09-12 15:01
 **/

@Component
public class ReceiveHandler {

    //监听email队列
    @RabbitListener(queues = {RabbitmqConfig.QUEUE_INFORM_EMAIL})
    public void receive_email(String msg, Message message, Channel channel) {
        System.out.println("email" + msg);
        System.out.println(message);
        System.out.println(channel);
    }


    //监听sms队列
    @RabbitListener(queues = {RabbitmqConfig.QUEUE_INFORM_SMS})
    public void receive_sms(String msg, Message message, Channel channel) {
        System.out.println("sms" + msg);
        System.out.println(message);
        System.out.println(channel);
    }

}
