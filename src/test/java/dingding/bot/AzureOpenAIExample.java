package dingding.bot;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.*;
import com.azure.core.credential.AzureKeyCredential;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AzureOpenAIExample {

    public static void main(String[] args) {
        try {
            String question = "你好";
            String answer = getAnswerFromAzureOpenAI(question);
            System.out.println("Answer: " + answer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getAnswerFromAzureOpenAI(String question) throws IOException {
        String apiKey = "23984102e5474856b600f0e08445e845";
        String endpoint = "https://dingbot-1.openai.azure.com/";
        String deploymentOrModelId = "dingbot-gpt";

        //建立openai客户端
        OpenAIClient client = new OpenAIClientBuilder()
                .endpoint(endpoint)
                .credential(new AzureKeyCredential(apiKey))
                .buildClient();

        //构建list数据：用户user提供的问题/自定义系统答案，调用部署模型
        List<ChatMessage> chatMessages = new ArrayList<>();
        chatMessages.add(new ChatMessage(ChatRole.USER).setContent(question));
        ChatCompletions chatCompletions = client.getChatCompletions(deploymentOrModelId, new ChatCompletionsOptions(chatMessages));

        //获取答案
        String answer = "";
        for (ChatChoice choice : chatCompletions.getChoices()) {
            ChatMessage message = choice.getMessage();
            System.out.printf("Index: %d, Chat Role: %s.%n", choice.getIndex(), message.getRole());
            System.out.println("Message:");
            System.out.println(message.getContent());
            answer = message.getContent();
        }
        CompletionsUsage usage = chatCompletions.getUsage();
        System.out.printf("Usage: number of prompt token is %d, "
                        + "number of completion token is %d, and number of total tokens in request and response is %d.%n",
                usage.getPromptTokens(), usage.getCompletionTokens(), usage.getTotalTokens());


        return answer;
    }
}
