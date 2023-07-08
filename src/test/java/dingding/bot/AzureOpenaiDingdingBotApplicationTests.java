package dingding.bot;

import dingding.bot.config.JedisConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

@SpringBootTest
class AzureOpenaiDingdingBotApplicationTests {

    @Autowired
    JedisConfig jedisConfig;

    @Test
    void contextLoads() {
        jedisConfig.getJedis().set("k","1");
        ScanResult<String> scan = jedisConfig.getJedis().scan("0", new ScanParams().match("*"));
        System.out.println(scan.getCursor());
    }

}
