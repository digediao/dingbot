package dingding.bot.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


/**
 *  openai_apikey=a8ce2b68079f4c268db21a1aae173bcf
 * openai_endpoint=https://dingbot-openai.openai.azure.com/
 * openai_deployment=dingbot-gpt35
 */

public class azure_openai_gpt35 {
    public static String openai_apikey;
    public static String openai_endpoint;
    public static String openai_deployment;
    static {
        // 加载配置文件
        Properties properties = new Properties();
        try {
            FileInputStream openai_properties = new FileInputStream("config.properties");
            properties.load(openai_properties);
            openai_properties.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 获取配置值
        openai_apikey = properties.getProperty("openai_apikey");
        openai_endpoint = properties.getProperty("openai_endpoint");
        openai_deployment = properties.getProperty("openai_deployment");
    }
}
