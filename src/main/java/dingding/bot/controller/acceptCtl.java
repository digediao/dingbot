package dingding.bot.controller;

import dingding.bot.config.DisableCertificateValidation;
import dingding.bot.pojo.Payload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import dingding.bot.service.dingbotService;

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
