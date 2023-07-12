package dingding.bot.handler;


import dingding.bot.pojo.Payload;
import dingding.bot.service.dingbotService;
import okhttp3.Request;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class RequestHandler {
    @Autowired
    private dingbotService dingbotService;
    //任务线程池
    private final ThreadPoolTaskExecutor taskExecutor;
    //请求队列
    private final BlockingQueue<Runnable> requestQueue;
    private final CloseableHttpClient httpClient;



    @Autowired
    public RequestHandler(@Qualifier("taskExecutor") ThreadPoolTaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
        this.requestQueue = new LinkedBlockingQueue<>();
        this.httpClient = HttpClients.createDefault();

        new Thread(() -> {
            while (true) {
                try {
                    Runnable request = requestQueue.take();
                    taskExecutor.submit(request);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Async("taskExecutor") // 使用taskExecutor执行异步任务
    public void handle(Payload payload) {
        requestQueue.offer(() -> processRequest(payload));
    }

    private void processRequest(Payload payload) {
        String answer = dingbotService.getAnswerFromAzureOpenAI(payload.getText().getContent(), payload);
        dingbotService.sendAnswerToDingTalk(answer, payload, httpClient);
    }

}
