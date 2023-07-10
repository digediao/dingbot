package dingding.bot.service;


import com.baomidou.mybatisplus.extension.service.IService;
import dingding.bot.entity.azure_openai_history;
import dingding.bot.pojo.Payload;

public interface dingbotService {
    void getAnswerFromAzureOpenAI(String question, Payload payload);
}
