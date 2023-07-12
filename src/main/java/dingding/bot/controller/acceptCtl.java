package dingding.bot.controller;

import dingding.bot.config.DisableCertificateValidation;
import dingding.bot.handler.RequestHandler;
import dingding.bot.pojo.Payload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import dingding.bot.service.dingbotService;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;

@RestController
@Slf4j
public class acceptCtl {
    @Autowired
    private dingbotService dingbotService;
    @Autowired
    private RequestHandler handler;

    /**
     * 获取@机器人的post请求
     * @param payload
     */
    @PostMapping("/webhook")
    public void handleDingTalkCallback(@RequestBody Payload payload) {
        log.info("payload信息:"+payload);
        //本地测试：禁用校验
        try {
            DisableCertificateValidation.disableCertificateValidation();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        handler.handle(payload);
    }



}
