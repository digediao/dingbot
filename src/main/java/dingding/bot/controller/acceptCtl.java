package dingding.bot.controller;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.*;
import com.azure.core.credential.AzureKeyCredential;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import dingding.bot.config.DisableCertificateValidation;
import dingding.bot.config.JedisConfig;
import dingding.bot.pojo.Payload;
import dingding.bot.entity.azure_openai_history;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import dingding.bot.service.dingbotService;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static dingding.bot.util.redis_Constant.*;
import static dingding.bot.util.azure_openai_gpt35.*;

@RestController
public class acceptCtl {

    @Autowired
    private dingbotService dingbotService;

    /**
     * 获取@机器人的post请求
     * @param payload
     */
    @PostMapping("/webhook")
    public void handleDingTalkCallback(@RequestBody Payload payload) {
        //本地测试：禁用校验
        try {
            DisableCertificateValidation.disableCertificateValidation();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(payload);
        // 1、在这里处理钉钉机器人的回调消息
        String question = payload.getText().getContent();
        // 2、调用OpenAI API获取回答，最后发送回答到钉钉机器人
        dingbotService.getAnswerFromAzureOpenAI(question, payload);
    }




}
