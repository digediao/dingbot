package dingding.bot.listener;


import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class rabbitmqCustomer {
    @RabbitListener(queues = "queue")
    public void receiveMessage(String message) {
        System.out.println("Received message: " + message);
    }
}
