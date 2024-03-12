package example;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.image.CreateImageRequest;
import com.theokanning.openai.model.Model;
import com.theokanning.openai.service.AzureOpenAiService;
import com.theokanning.openai.service.OpenAiService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class AzureOpenAiApiExample {
    public static void main(String... args) {
        String token = "6d7f9a1abfc140fb822f923c15b185f6";
        String deployment = "sandbox";
        String baseUrl = "https://ktl-gpt-sandbox.openai.azure.com";
        final String apiVersion20221201 = "2022-12-01";
        final String apiVersion20230315 = "2023-03-15-preview";
        AzureOpenAiService service = new AzureOpenAiService(token, baseUrl, deployment, Duration.ZERO);

        System.out.println("Streaming chat completion...");
        final List<ChatMessage> messages = new ArrayList<>();
        final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "Tell me a joke.");
        messages.add(systemMessage);
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
                .n(1)
                .maxTokens(50)
                .logitBias(new HashMap<>())
                .build();
        service.streamChatCompletion(chatCompletionRequest, apiVersion20230315)
            .doOnError(Throwable::printStackTrace)
            .blockingForEach(record -> System.out.print(record.getChoices().get(0).getMessage().getContent()));

        service.shutdownExecutor();                
    }
}