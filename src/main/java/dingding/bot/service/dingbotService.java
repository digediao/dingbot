package dingding.bot.service;


import dingding.bot.pojo.Payload;

public interface dingbotService {
    void getAnswerFromAzureOpenAI(String question, Payload payload);
}
