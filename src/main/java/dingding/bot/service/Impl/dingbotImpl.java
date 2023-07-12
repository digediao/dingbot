package dingding.bot.service.Impl;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.*;
import com.azure.core.credential.AzureKeyCredential;
import dingding.bot.pojo.Payload;
import dingding.bot.service.dingbotService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static dingding.bot.util.azure_openai_gpt35.*;

@Service
public class dingbotImpl implements dingbotService {
    //全局存储历史
    private static final List<String> history = new ArrayList<>();
    @Autowired
    RedisTemplate redisTemplate;
    
    /**
     * 根据提出的问题转发到azure openai中并返回答案
     *
     * @param question
     * @return
     */
    public String getAnswerFromAzureOpenAI(String question, Payload payload){
        //建立openai客户端
        OpenAIClient client = new OpenAIClientBuilder()
                .endpoint(openai_endpoint)
                .credential(new AzureKeyCredential(openai_apikey))
                .buildClient();

        //构建历史对话,将之前历史对话清空再次获取全部历史对话
        history.clear();
        Map<String, String> hashData = redisTemplate.opsForHash().entries(payload.getSessionWebhook());
        for(Map.Entry<String,String> entry : hashData.entrySet()){
            if(hashData.size()==0) break;
            history.add(entry.getKey());
            history.add(entry.getValue());
        }

        //【prompt】 将历史对话拼接成一个字符串,构建提示消息
        String context = String.join("\n", history);
        List<ChatMessage> promptMessages = new ArrayList<>();
        promptMessages.add(new ChatMessage(ChatRole.SYSTEM).setContent(context));

        //【completion】 构建新的对话列表，包含用户的新问题
        List<ChatMessage> conversation = new ArrayList<>(promptMessages);
        conversation.add(new ChatMessage(ChatRole.USER).setContent("Question: " + question));

        // 发送请求给 OpenAI
        ChatCompletions chatCompletions = client.getChatCompletions(openai_deployment, new ChatCompletionsOptions(conversation));

        // 获取回答，并将问答填入数据库
        String answer = "";
        for (ChatChoice choice : chatCompletions.getChoices()) {
            ChatMessage message = choice.getMessage();
            System.out.printf("Index: %d, Chat Role: %s.%n", choice.getIndex(), message.getRole());
            System.out.println("Message:");
            System.out.println(message.getContent());
            answer = message.getContent();
        }
        //插入当前问答历史数据，并设置与机器人相同的存亡时间
        redisTemplate.opsForHash().put(payload.getSessionWebhook(),question,answer);
        redisTemplate.expire(payload.getSessionWebhook(),payload.getSessionWebhookExpiredTime(), TimeUnit.MILLISECONDS);

        //打印信息
        CompletionsUsage usage = chatCompletions.getUsage();
        System.out.printf("Usage: number of prompt token is %d, "
                        + "number of completion token is %d, and number of total tokens in request and response is %d.%n",
                usage.getPromptTokens(), usage.getCompletionTokens(), usage.getTotalTokens());

//        sendAnswerToDingTalk(answer, payload);
        return answer;
    }

    /**
     * 发送响应数据到钉钉机器人
     * 消息类型为：text
     * @param answer
     */
    public void sendAnswerToDingTalk(String answer, Payload payload, CloseableHttpClient httpClient) {
        String senderNick = payload.getSenderNick();
        // 构造回答消息——msgtype:text/markdown/image/link/actionCard
        String jsonPayload = "{\"msgtype\": \""+"text"+"\", \"text\": {\"content\": \"" + "@" + senderNick+" ,"+answer + "\"}}";

//        HttpClient方法
        try {
            String dingTalkUrl = payload.getSessionWebhook();
            HttpPost httpPost = new HttpPost(dingTalkUrl);
            httpPost.setHeader("Content-Type", "application/json");

            // 构建请求体
            StringEntity requestEntity = new StringEntity(jsonPayload, "UTF-8");
            httpPost.setEntity(requestEntity);

            // 发送请求并获取响应
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();

            // 处理响应
            if (responseEntity != null) {
                String responseBody = EntityUtils.toString(responseEntity);
                // 处理响应内容
                System.out.println(responseBody);
            }
            EntityUtils.consume(responseEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
