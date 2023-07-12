package dingding.bot.service;


import dingding.bot.pojo.Payload;
import org.apache.http.impl.client.CloseableHttpClient;

public interface dingbotService {
    String getAnswerFromAzureOpenAI(String question, Payload payload);

    void sendAnswerToDingTalk(String answer, Payload payload, CloseableHttpClient httpClient);
}
