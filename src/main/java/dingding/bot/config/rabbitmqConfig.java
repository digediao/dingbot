package dingding.bot.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static dingding.bot.util.rabbitmqName.*;

@Configuration
public class rabbitmqConfig {
    @Value("${spring.rabbitmq.host}")
    private String host;
    @Value("${spring.rabbitmq.port}")
    private int port;
    @Value("${spring.rabbitmq.username}")
    private String username;
    @Value("${spring.rabbitmq.password}")
    private String password;

    //配置ConnectionFactory
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }
    //配置rabbitmqTemplate
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        // 设置消息转换器，默认使用的是SimpleMessageConverter
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
    @Bean
    public MessageConverter jsonMessageConverter() {
        // 使用Jackson2JsonMessageConverter将消息转换为JSON格式
        return new Jackson2JsonMessageConverter();
    }

    /**
     * dingbot的队列、交换机及routing-key
     * @return
     */
    @Bean
    public Queue dingbot_queue() {
        return new Queue(DINGBOT_QUEUE);
    }
    @Bean
    public DirectExchange dingbot_exchange(){
        return new DirectExchange(DINGBOT_EXCHANGE);
    }
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(dingbot_queue()).to(dingbot_exchange()).with(DINGBOT_ROUTING_KEY);
    }
}
