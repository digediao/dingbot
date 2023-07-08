package dingding.bot;

import dingding.bot.config.JedisConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class AzureOpenaiDingdingBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(AzureOpenaiDingdingBotApplication.class, args);
    }

}
