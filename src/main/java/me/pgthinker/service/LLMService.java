package me.pgthinker.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Map;

/**
 * @author NingNing0111
 * @package me.pgthinker.service
 * @className me.pgthinker.service.LLMService
 */
@Service
public class LLMService {

    @Value("classpath:prompt/mind_expert.txt")
    public Resource mindExpertPrompt;

    private final ChatModel chatModel;

    public LLMService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public Flux<ChatResponse> generateOutlineWithTopic(String topic) {
        ChatClient chatClient = ChatClient.builder(chatModel).build();
        PromptTemplate template = PromptTemplate.builder().resource(mindExpertPrompt).variables(Map.of("topic", topic)).build();
        Message userMessage = template.createMessage();
        return chatClient.prompt(Prompt.builder().messages(userMessage).build()).stream().chatResponse();
    }

}
