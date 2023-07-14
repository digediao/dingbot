package test0;

import dingding.bot.AzureOpenaiDingdingBotApplication;
import dingding.bot.pojo.Payload;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = AzureOpenaiDingdingBotApplication.class)
public class test1 {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    public void test01(){
        String msg = "你好";
        rabbitTemplate.convertAndSend("dingbot_exchange", "dingbot_routing_key", new Payload());
    }

}
