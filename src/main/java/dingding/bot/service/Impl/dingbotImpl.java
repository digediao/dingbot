package dingding.bot.service.Impl;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.*;
import com.azure.core.credential.AzureKeyCredential;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import dingding.bot.controller.acceptCtl;
import dingding.bot.entity.azure_openai_history;
import dingding.bot.pojo.Payload;
import dingding.bot.service.dingbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dingding.bot.mapper.dingbotMP;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static dingding.bot.util.azure_openai_gpt35.*;
import static dingding.bot.util.redis_Constant.DINGDING_SESSIONWEBHOOK;
import static dingding.bot.util.redis_Constant.OPENAI_ANSWER;

@Service
public class dingbotImpl implements dingbotService {
    //全局存储历史
    private static final List<String> history = new ArrayList<>();
    @Autowired
    dingbotMP dingbotMP;
    
    /**
     * 根据提出的问题转发到azure openai中并返回答案
     * @param question
     * @return
     */
    public void getAnswerFromAzureOpenAI(String question, Payload payload){
        //建立openai客户端
        OpenAIClient client = new OpenAIClientBuilder()
                .endpoint(openai_endpoint)
                .credential(new AzureKeyCredential(openai_apikey))
                .buildClient();

        //构建历史对话,将之前历史对话清空再次获取全部历史对话
//        acceptCtl.history.clear();
//        Map<String, String> hashData = jedisConfig.getJedis().hgetAll(payload.getSessionWebhook());
//        for(Map.Entry<String,String> entry : hashData.entrySet()){
//            acceptCtl.history.add("Question: "+entry.getKey());
//            acceptCtl.history.add("Answer: "+entry.getValue());
//        }
        List<azure_openai_history> histories = dingbotMP.selectList(new QueryWrapper<azure_openai_history>().isNotNull(OPENAI_ANSWER).eq(DINGDING_SESSIONWEBHOOK, payload.getSessionWebhook()));
        for(azure_openai_history entity : histories){
            history.add("Question: "+entity.getQuestion());
            history.add("Answer: "+entity.getAnswer());
        }

        System.out.println(history);
        // 将历史对话拼接成一个字符串
        String context = String.join("\n", history);

        // 构建提示消息
        List<ChatMessage> promptMessages = new ArrayList<>();
        promptMessages.add(new ChatMessage(ChatRole.SYSTEM).setContent(context));

        // 构建新的对话列表，包含用户的新问题
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
//        jedisConfig.getJedis().hset(payload.getSessionWebhook(),question,answer);
//        jedisConfig.getJedis().expire(payload.getSessionWebhook(),payload.getSessionWebhookExpiredTime());
        azure_openai_history new_history = new azure_openai_history();
        new_history.setQuestion(question);
        new_history.setAnswer(answer);
        new_history.setSessionWebhook(payload.getSessionWebhook());
        dingbotMP.insert(new_history);

        //打印信息
        CompletionsUsage usage = chatCompletions.getUsage();
        System.out.printf("Usage: number of prompt token is %d, "
                        + "number of completion token is %d, and number of total tokens in request and response is %d.%n",
                usage.getPromptTokens(), usage.getCompletionTokens(), usage.getTotalTokens());

//        jedisConfig.getJedis().close();
        sendAnswerToDingTalk(answer, payload.getSessionWebhook());
    }

    /**
     * 发送响应数据到钉钉机器人
     * @param answer
     */
    private void sendAnswerToDingTalk(String answer,String sessionWebhook) {
        // 构造回答消息——msgtype:text/markdown/image/link/actionCard
        String jsonPayload = "{\"msgtype\": \"text\", \"text\": {\"content\": \"" + answer + "\"}}";

        try {
            URL url = new URL(sessionWebhook);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);
            // 设置连接超时为10秒
            conn.setConnectTimeout(10000);
            // 设置读取超时为10秒
            conn.setReadTimeout(10000);

            try (DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream())) {
                outputStream.write(jsonPayload.getBytes(StandardCharsets.UTF_8));
            }
            System.out.println("发送数据成功："+conn.getResponseCode());
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
