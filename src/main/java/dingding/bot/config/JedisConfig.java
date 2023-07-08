package dingding.bot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;

//@Data
//@Component
//@ConfigurationProperties(prefix = "jedis")
public class JedisConfig{
//    @Value("${jedis.host}")
//    private String host;
//    @Value("${jedis.port}")
//    private Integer port;
////    @Value("${jedis.password}")
////    private String password;
//    @Value("${jedis.waitTime}")
//    private Integer waitTime;
//
//    private Jedis jedis;
//
//    public Jedis getJedis() {
//        jedis = new Jedis(host,port,waitTime);
////        jedis.auth(password);
//        return jedis;
//    }
}
