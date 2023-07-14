package dingding.bot.listener;


import dingding.bot.pojo.Payload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static dingding.bot.util.rabbitmqName.DINGBOT_QUEUE;

@Component
@Slf4j
public class rabbitmqCustomer {
    @RabbitListener(queues = DINGBOT_QUEUE)
    public void receiveMessage(Object o) {
        log.info(DINGBOT_QUEUE+"队列监听成功！"+o);
    }
}
